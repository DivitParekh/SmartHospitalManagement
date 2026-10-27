import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Generates a readable text prescription from the saved hospital database record. */
public final class PrescriptionFileExporter {
    private PrescriptionFileExporter() {}

    public static Path exportPrescriptionsForRecord(int recordId)
            throws IOException {
        if (recordId <= 0) {
            throw new IllegalArgumentException("Medical record ID must be positive.");
        }

        String sql = """
                SELECT p.prescription_id, p.prescription_date, p.instructions,
                       mr.record_id, mr.record_date, mr.diagnosis,
                       pt.name AS patient_name, pt.age AS patient_age,
                       pt.gender AS patient_gender, pt.blood_group,
                       d.name AS doctor_name, d.specialization,
                       m.name AS medicine_name, pi.dosage, pi.duration, pi.quantity
                FROM prescriptions p
                JOIN medical_records mr ON p.record_id = mr.record_id
                JOIN patients pt ON mr.patient_id = pt.patient_id
                JOIN doctors d ON mr.doctor_id = d.doctor_id
                LEFT JOIN prescription_items pi ON p.prescription_id = pi.prescription_id
                LEFT JOIN medicines m ON pi.medicine_id = m.medicine_id
                WHERE mr.record_id = ?
                ORDER BY p.prescription_date DESC, p.prescription_id DESC, pi.item_id
                """;

        List<String> lines = new ArrayList<>();
        int currentPrescriptionId = -1;
        boolean found = false;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    found = true;
                    int prescriptionId = rs.getInt("prescription_id");
                    if (prescriptionId != currentPrescriptionId) {
                        if (currentPrescriptionId != -1) {
                            lines.add("");
                            lines.add("==================================================");
                            lines.add("");
                        }
                        currentPrescriptionId = prescriptionId;
                        lines.add("                 HOSPITAL PRESCRIPTION");
                        lines.add("==================================================");
                        lines.add("Prescription ID : " + prescriptionId);
                        lines.add("Prescription Date: " + safe(rs.getString("prescription_date")));
                        lines.add("Medical Record ID: " + rs.getInt("record_id"));
                        lines.add("Record Date      : " + safe(rs.getString("record_date")));
                        lines.add("--------------------------------------------------");
                        lines.add("Patient          : " + safe(rs.getString("patient_name")));
                        lines.add("Age / Gender     : " + safe(rs.getString("patient_age"))
                                + " / " + safe(rs.getString("patient_gender")));
                        lines.add("Blood Group      : " + safe(rs.getString("blood_group")));
                        lines.add("Doctor           : " + safe(rs.getString("doctor_name")));
                        lines.add("Specialization   : " + safe(rs.getString("specialization")));
                        lines.add("Diagnosis        : " + safe(rs.getString("diagnosis")));
                        lines.add("Instructions     : " + safe(rs.getString("instructions")));
                        lines.add("");
                        lines.add("PRESCRIBED MEDICINES");
                        lines.add("--------------------------------------------------");
                    }
                    String medicine = rs.getString("medicine_name");
                    if (medicine == null) {
                        lines.add("No medicine items recorded for this prescription.");
                    } else {
                        lines.add("Medicine : " + medicine);
                        lines.add("Dosage   : " + safe(rs.getString("dosage")));
                        lines.add("Duration : " + safe(rs.getString("duration")));
                        lines.add("Quantity : " + rs.getInt("quantity"));
                        lines.add("--------------------------------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            throw new IOException("Unable to retrieve prescription details: " + e.getMessage(), e);
        }

        if (!found) {
            throw new IOException("No saved prescriptions were found for medical record " + recordId + ".");
        }

        lines.add("");
        lines.add("Note: This file is generated from the hospital system's saved records.");
        lines.add("Please verify the prescription with the treating doctor before use.");

        Path directory = Paths.get("exports");
        Files.createDirectories(directory);
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path file = directory.resolve("prescriptions_record_" + recordId + "_" + timestamp + ".txt");
        Files.write(file, lines, StandardCharsets.UTF_8);
        return file.toAbsolutePath();
    }

    private static String safe(String value) {
        return value == null || value.isBlank() ? "Not specified" : value;
    }
}
