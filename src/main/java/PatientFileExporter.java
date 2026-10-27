import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Exports a patient's details to a CSV file using Java file handling. */
public class PatientFileExporter {
    private PatientFileExporter() {}

    public static Path exportPatient(Patient patient) throws IOException {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        Path directory = Paths.get("exports");
        Files.createDirectories(directory);
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path file = directory.resolve("patient_" + patient.getId() + "_"
                + timestamp + ".csv");

        List<String> lines = List.of(
                "Patient ID,Name,Age,Phone,Gender,Address,Blood Group",
                csv(patient.getId()) + "," + csv(patient.getName()) + ","
                        + csv(patient.getAge()) + "," + csv(patient.getPhone()) + ","
                        + csv(patient.getGender()) + "," + csv(patient.getAddress()) + ","
                        + csv(patient.getBloodGroup()));
        Files.write(file, lines, StandardCharsets.UTF_8);
        return file.toAbsolutePath();
    }

    private static String csv(Object value) {
        String text = value == null ? "" : value.toString();
        return "\"" + text.replace("\"", "\"\"") + "\"";
    }
}
