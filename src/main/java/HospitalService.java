
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;

 
 
public class HospitalService implements Searchable<Patient> {

    // ==================== PATIENT MANAGEMENT ====================

    // Register a new patient
    public boolean registerPatient(Patient patient) throws SQLException {
        String sql = """
                INSERT INTO patients
                (name, age, gender, phone, address, blood_group)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, patient.getName());
            ps.setInt(2, patient.getAge());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getPhone());
            ps.setString(5, patient.getAddress());
            ps.setString(6, patient.getBloodGroup());

            return ps.executeUpdate() == 1;
        }
    }

    // View all patients
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patients ORDER BY patient_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        }

        return patients;
    }

    // Search patient by ID (used internally)
    public Patient getPatientById(int patientId) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPatient(rs);
                }
            }
        }

        return null;
    }

    // Search patients by full or partial name
    public List<Patient> searchPatientsByName(String name)
            throws SQLException {

        List<Patient> patients = new ArrayList<>();

        String sql = """
                SELECT * FROM patients
                WHERE name LIKE ?
                ORDER BY name, patient_id
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapPatient(rs));
                }
            }
        }

        return patients;
    }

    // Interface implementation: search patients by name.
    @Override
    public List<Patient> search(String keyword) throws SQLException {
        return searchPatientsByName(keyword);
    }

    // Method overloading: search by name and gender together.
    public List<Patient> searchPatientsByName(String name, String gender)
            throws SQLException {

        List<Patient> patients = new ArrayList<>();
        String sql = """
                SELECT * FROM patients
                WHERE name LIKE ? AND gender = ?
                ORDER BY name, patient_id
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name.trim() + "%");
            ps.setString(2, gender.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapPatient(rs));
                }
            }
        }
        return patients;
    }

    // Update patient details
    public boolean updatePatient(Patient patient) throws SQLException {
        String sql = """
                UPDATE patients
                SET name = ?, age = ?, gender = ?, phone = ?,
                    address = ?, blood_group = ?
                WHERE patient_id = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, patient.getName());
            ps.setInt(2, patient.getAge());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getPhone());
            ps.setString(5, patient.getAddress());
            ps.setString(6, patient.getBloodGroup());
            ps.setInt(7, patient.getId());

            return ps.executeUpdate() == 1;
        }
    }

    // Convert database row into a Patient object
    private Patient mapPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getInt("patient_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("phone"),
                rs.getString("gender"),
                rs.getString("address"),
                rs.getString("blood_group")
        );
    }

    // ==================== DOCTOR MANAGEMENT ====================

    // Register a new doctor
    public boolean registerDoctor(Doctor doctor) throws SQLException {
        String sql = """
                INSERT INTO doctors
                (name, age, phone, specialization, available)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, doctor.getName());
            ps.setInt(2, doctor.getAge());
            ps.setString(3, doctor.getPhone());
            ps.setString(4, doctor.getSpecialization());
            ps.setBoolean(5, doctor.isAvailable());

            return ps.executeUpdate() == 1;
        }
    }

    // View all doctors
    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();

        String sql = "SELECT * FROM doctors ORDER BY doctor_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }

        return doctors;
    }

    // Search doctors by specialization
    public List<Doctor> searchDoctorsBySpecialization(
            String specialization) throws SQLException {

        List<Doctor> doctors = new ArrayList<>();

        String sql = """
                SELECT * FROM doctors
                WHERE specialization LIKE ?
                ORDER BY name
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + specialization.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        }

        return doctors;
    }

    // Search doctors by full or partial name
    public List<Doctor> searchDoctorsByName(String name)
            throws SQLException {

        List<Doctor> doctors = new ArrayList<>();

        String sql = """
                SELECT * FROM doctors
                WHERE name LIKE ?
                ORDER BY name, doctor_id
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        }

        return doctors;
    }

    // Convert database row into a Doctor object
    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        return new Doctor(
                rs.getInt("doctor_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("phone"),
                rs.getString("specialization"),
                rs.getBoolean("available")
        );
    }

    // ==================== APPOINTMENT MANAGEMENT ====================

    // Schedule an appointment using the selected patient and doctor.
    // IDs are used internally; the user selects names in App.java.
    public boolean scheduleAppointment(Appointment appointment)
            throws SQLException {

        // Verify that the patient exists
        if (getPatientById(appointment.getPatientId()) == null) {
            System.out.println("Selected patient no longer exists.");
            return false;
        }

        String doctorSql = """
                SELECT available
                FROM doctors
                WHERE doctor_id = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement doctorPs =
                     con.prepareStatement(doctorSql)) {

            doctorPs.setInt(1, appointment.getDoctorId());

            try (ResultSet rs = doctorPs.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Selected doctor does not exist.");
                    return false;
                }

                if (!rs.getBoolean("available")) {
                    System.out.println("Selected doctor is unavailable.");
                    return false;
                }
            }

            // Prevent booking the same doctor at an already-booked
            // date and time. Cancelled appointments do not block slots.
            String conflictSql = """
                    SELECT COUNT(*)
                    FROM appointments
                    WHERE doctor_id = ?
                      AND appointment_date = ?
                      AND appointment_time = ?
                      AND status = 'SCHEDULED'
                    """;

            try (PreparedStatement conflictPs =
                         con.prepareStatement(conflictSql)) {

                conflictPs.setInt(1, appointment.getDoctorId());
                conflictPs.setDate(2, java.sql.Date.valueOf(
                        appointment.getAppointmentDate()));
                conflictPs.setTime(3, java.sql.Time.valueOf(
                        appointment.getAppointmentTime()));

                try (ResultSet rs = conflictPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println(
                                "This doctor already has an appointment "
                                + "at that date and time."
                        );
                        return false;
                    }
                }
            }

            String sql = """
                    INSERT INTO appointments
                    (patient_id, doctor_id, appointment_date,
                     appointment_time, status, reason)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, appointment.getPatientId());
                ps.setInt(2, appointment.getDoctorId());
                ps.setDate(3, java.sql.Date.valueOf(
                        appointment.getAppointmentDate()));
                ps.setTime(4, java.sql.Time.valueOf(
                        appointment.getAppointmentTime()));
                ps.setString(5, appointment.getStatus());
                ps.setString(6, appointment.getReason());

                return ps.executeUpdate() == 1;
            }
        }
    }

    // View all appointments with patient and doctor names
    public void viewAllAppointments() throws SQLException {
        String sql = """
                SELECT a.appointment_id, a.patient_id,
                       p.name AS patient_name,
                       a.doctor_id, d.name AS doctor_name,
                       d.specialization,
                       a.appointment_date, a.appointment_time,
                       a.status, a.reason
                FROM appointments a
                JOIN patients p ON a.patient_id = p.patient_id
                JOIN doctors d ON a.doctor_id = d.doctor_id
                ORDER BY a.appointment_date, a.appointment_time
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("\n================================");
                System.out.println("Appointment ID: "
                        + rs.getInt("appointment_id"));
                System.out.println("Patient: "
                        + rs.getString("patient_name"));
                System.out.println("Doctor: Dr. "
                        + rs.getString("doctor_name"));
                System.out.println("Specialization: "
                        + rs.getString("specialization"));
                System.out.println("Date: "
                        + rs.getDate("appointment_date"));
                System.out.println("Time: "
                        + rs.getTime("appointment_time"));
                System.out.println("Status: "
                        + rs.getString("status"));
                System.out.println("Reason: "
                        + rs.getString("reason"));
            }

            if (!found) {
                System.out.println("No appointments found.");
            }
        }
    }
    
    // Update an appointment's status
    public boolean updateAppointmentStatus(
            int appointmentId, String newStatus) throws SQLException {

        if (!newStatus.equals("COMPLETED")
                && !newStatus.equals("MISSED")
                && !newStatus.equals("CANCELLED")) {
            throw new IllegalArgumentException(
                    "Invalid appointment status.");
        }

        String sql = """
                UPDATE appointments
                SET status = ?
                WHERE appointment_id = ?
                  AND status = 'SCHEDULED'
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, appointmentId);

            return ps.executeUpdate() == 1;
        }
    }

    // Check whether an appointment's date and time have passed
    private boolean isAppointmentPast(
            java.sql.Date date, java.sql.Time time) {

        java.time.LocalDateTime appointmentDateTime =
                java.time.LocalDateTime.of(
                        date.toLocalDate(), time.toLocalTime());

        return appointmentDateTime.isBefore(
                java.time.LocalDateTime.now());
    }

    
    // ==================== MEDICAL RECORD MANAGEMENT ====================

    // Add a medical record for a patient
    public boolean addMedicalRecord(MedicalRecord record)
            throws SQLException {

        // Verify that the patient exists
        if (getPatientById(record.getPatientId()) == null) {
            System.out.println("Patient does not exist.");
            return false;
        }

        // Verify that the doctor exists
        String doctorSql =
                "SELECT doctor_id FROM doctors WHERE doctor_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement doctorPs =
                     con.prepareStatement(doctorSql)) {

            doctorPs.setInt(1, record.getDoctorId());

            try (ResultSet rs = doctorPs.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Doctor does not exist.");
                    return false;
                }
            }

            // If an appointment ID is provided, verify it
            if (record.getAppointmentId() != null) {
                String appointmentSql = """
                        SELECT appointment_id
                        FROM appointments
                        WHERE appointment_id = ?
                          AND patient_id = ?
                          AND doctor_id = ?
                        """;

                try (PreparedStatement appointmentPs =
                             con.prepareStatement(appointmentSql)) {

                    appointmentPs.setInt(
                            1, record.getAppointmentId());
                    appointmentPs.setInt(
                            2, record.getPatientId());
                    appointmentPs.setInt(
                            3, record.getDoctorId());

                    try (ResultSet rs =
                                 appointmentPs.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println(
                                    "Appointment not found for "
                                    + "this patient and doctor.");
                            return false;
                        }
                    }
                }
            }

            String sql = """
                    INSERT INTO medical_records
                    (patient_id, doctor_id, appointment_id,
                     diagnosis, record_date)
                    VALUES (?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, record.getPatientId());
                ps.setInt(2, record.getDoctorId());

                if (record.getAppointmentId() == null) {
                    ps.setNull(3, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(3, record.getAppointmentId());
                }

                ps.setString(4, record.getDiagnosis());
                ps.setDate(5, java.sql.Date.valueOf(
                        record.getRecordDate()));

                return ps.executeUpdate() == 1;
            }
        }
    }

    // View medical records for a specific patient
    public void viewMedicalRecordsByPatient(int patientId)
            throws SQLException {

        String sql = """
                SELECT mr.record_id, mr.patient_id,
                       p.name AS patient_name,
                       mr.doctor_id, d.name AS doctor_name,
                       mr.appointment_id, mr.diagnosis,
                       mr.record_date
                FROM medical_records mr
                JOIN patients p
                    ON mr.patient_id = p.patient_id
                JOIN doctors d
                    ON mr.doctor_id = d.doctor_id
                WHERE mr.patient_id = ?
                ORDER BY mr.record_date DESC, mr.record_id DESC
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println("\n================================");
                    System.out.println("Record ID: "
                            + rs.getInt("record_id"));
                    System.out.println("Patient: "
                            + rs.getString("patient_name"));
                    System.out.println("Doctor: Dr. "
                            + rs.getString("doctor_name"));

                    int appointmentId =
                            rs.getInt("appointment_id");

                    System.out.println("Appointment ID: "
                            + (rs.wasNull()
                                    ? "Not linked" : appointmentId));

                    System.out.println("Diagnosis: "
                            + rs.getString("diagnosis"));
                    System.out.println("Record Date: "
                            + rs.getDate("record_date"));
                }

                if (!found) {
                    System.out.println(
                            "No medical records found for this patient.");
                }
            }
        }
    }

    // View all medical records
    public void viewAllMedicalRecords() throws SQLException {

        String sql = """
                SELECT mr.record_id, p.name AS patient_name,
                       d.name AS doctor_name, mr.appointment_id,
                       mr.diagnosis, mr.record_date
                FROM medical_records mr
                JOIN patients p
                    ON mr.patient_id = p.patient_id
                JOIN doctors d
                    ON mr.doctor_id = d.doctor_id
                ORDER BY mr.record_date DESC, mr.record_id DESC
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("\n================================");
                System.out.println("Record ID: "
                        + rs.getInt("record_id"));
                System.out.println("Patient: "
                        + rs.getString("patient_name"));
                System.out.println("Doctor: Dr. "
                        + rs.getString("doctor_name"));

                int appointmentId =
                        rs.getInt("appointment_id");

                System.out.println("Appointment ID: "
                        + (rs.wasNull()
                                ? "Not linked" : appointmentId));

                System.out.println("Diagnosis: "
                        + rs.getString("diagnosis"));
                System.out.println("Record Date: "
                        + rs.getDate("record_date"));
            }

            if (!found) {
                System.out.println("No medical records found.");
            }
        }
    }

    
    // ==================== TREATMENT MANAGEMENT ====================

    // Add a treatment to an existing medical record
    public boolean addTreatment(Treatment treatment)
            throws SQLException {

        // Verify that the medical record exists
        String checkSql = """
                SELECT record_id
                FROM medical_records
                WHERE record_id = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement checkPs =
                     con.prepareStatement(checkSql)) {

            checkPs.setInt(1, treatment.getRecordId());

            try (ResultSet rs = checkPs.executeQuery()) {
                if (!rs.next()) {
                    System.out.println(
                            "Medical record does not exist.");
                    return false;
                }
            }

            // Validate treatment details
            if (treatment.getDescription() == null
                    || treatment.getDescription().isBlank()) {
                System.out.println(
                        "Treatment description cannot be empty.");
                return false;
            }

            if (treatment.getCost() == null
                    || treatment.getCost().signum() < 0) {
                System.out.println(
                        "Treatment cost cannot be negative.");
                return false;
            }

            String sql = """
                    INSERT INTO treatments
                    (record_id, description, cost)
                    VALUES (?, ?, ?)
                    """;

            try (PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, treatment.getRecordId());
                ps.setString(
                        2, treatment.getDescription().trim());
                ps.setBigDecimal(3, treatment.getCost());

                return ps.executeUpdate() == 1;
            }
        }
    }

    // View treatments associated with a medical record
    public void viewTreatmentsByRecord(int recordId)
            throws SQLException {

        String sql = """
                SELECT t.treatment_id, t.record_id,
                       t.description, t.cost,
                       mr.patient_id, p.name AS patient_name,
                       d.name AS doctor_name
                FROM treatments t
                JOIN medical_records mr
                    ON t.record_id = mr.record_id
                JOIN patients p
                    ON mr.patient_id = p.patient_id
                JOIN doctors d
                    ON mr.doctor_id = d.doctor_id
                WHERE t.record_id = ?
                ORDER BY t.treatment_id
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, recordId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println("\n================================");
                    System.out.println("Treatment ID: "
                            + rs.getInt("treatment_id"));
                    System.out.println("Medical Record ID: "
                            + rs.getInt("record_id"));
                    System.out.println("Patient: "
                            + rs.getString("patient_name"));
                    System.out.println("Doctor: Dr. "
                            + rs.getString("doctor_name"));
                    System.out.println("Description: "
                            + rs.getString("description"));
                    System.out.println("Cost: Rs. "
                            + rs.getBigDecimal("cost"));
                }

                if (!found) {
                    System.out.println(
                            "No treatments found for this medical record.");
                }
            }
        }
    }

    // View all treatments
    public void viewAllTreatments() throws SQLException {

        String sql = """
                SELECT t.treatment_id, t.record_id,
                       t.description, t.cost,
                       p.name AS patient_name,
                       d.name AS doctor_name
                FROM treatments t
                JOIN medical_records mr
                    ON t.record_id = mr.record_id
                JOIN patients p
                    ON mr.patient_id = p.patient_id
                JOIN doctors d
                    ON mr.doctor_id = d.doctor_id
                ORDER BY t.treatment_id DESC
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("\n================================");
                System.out.println("Treatment ID: "
                        + rs.getInt("treatment_id"));
                System.out.println("Medical Record ID: "
                        + rs.getInt("record_id"));
                System.out.println("Patient: "
                        + rs.getString("patient_name"));
                System.out.println("Doctor: Dr. "
                        + rs.getString("doctor_name"));
                System.out.println("Description: "
                        + rs.getString("description"));
                System.out.println("Cost: Rs. "
                        + rs.getBigDecimal("cost"));
            }

            if (!found) {
                System.out.println("No treatments found.");
            }
        }
    }
    
    // Get medical records belonging to a specific patient
    public List<MedicalRecord> getMedicalRecordsByPatient(int patientId)
            throws SQLException {

        List<MedicalRecord> records = new ArrayList<>();

        String sql = """
                SELECT record_id, patient_id, doctor_id,
                       appointment_id, diagnosis, record_date
                FROM medical_records
                WHERE patient_id = ?
                ORDER BY record_date DESC, record_id DESC
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int appointmentValue =
                            rs.getInt("appointment_id");

                    Integer appointmentId = rs.wasNull()
                            ? null : appointmentValue;

                    records.add(new MedicalRecord(
                            rs.getInt("record_id"),
                            rs.getInt("patient_id"),
                            rs.getInt("doctor_id"),
                            appointmentId,
                            rs.getString("diagnosis"),
                            rs.getDate("record_date").toLocalDate()
                    ));
                }
            }
        }

        return records;
    }

    
    // Add a new medicine
    public void addMedicine(Medicine medicine) throws SQLException {
        String sql = """
                INSERT INTO medicines (name, unit_price, stock_quantity)
                VALUES (?, ?, ?)
                """;

        if (medicine.getName() == null
                || medicine.getName().isBlank()) {
            System.out.println("Medicine name cannot be empty.");
            return;
        }

        if (medicine.getUnitPrice() == null
                || medicine.getUnitPrice().signum() < 0) {
            System.out.println("Medicine price cannot be negative.");
            return;
        }

        if (medicine.getStockQuantity() < 0) {
            System.out.println("Stock quantity cannot be negative.");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, medicine.getName().trim());
            ps.setBigDecimal(2, medicine.getUnitPrice());
            ps.setInt(3, medicine.getStockQuantity());

            ps.executeUpdate();
            System.out.println("Medicine added successfully.");
        }
    }

    // View all medicines
    public void viewAllMedicines() throws SQLException {
        String sql = """
                SELECT medicine_id, name, unit_price, stock_quantity
                FROM medicines
                ORDER BY medicine_id
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== MEDICINE INVENTORY =====");

            boolean found = false;

            while (rs.next()) {
                found = true;

                Medicine medicine = new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getString("name"),
                        rs.getBigDecimal("unit_price"),
                        rs.getInt("stock_quantity")
                );

                medicine.displayDetails();
            }

            if (!found) {
                System.out.println("No medicines found.");
            }
        }
    }

    // Search medicines by name
    public List<Medicine> searchMedicinesByName(String name)
            throws SQLException {

        List<Medicine> medicines = new ArrayList<>();

        String sql = """
                SELECT medicine_id, name, unit_price, stock_quantity
                FROM medicines
                WHERE name LIKE ?
                ORDER BY name
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    medicines.add(new Medicine(
                            rs.getInt("medicine_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("unit_price"),
                            rs.getInt("stock_quantity")
                    ));
                }
            }
        }

        return medicines;
    }

    // Update medicine details
    public boolean updateMedicine(Medicine medicine)
            throws SQLException {

        String sql = """
                UPDATE medicines
                SET name = ?, unit_price = ?, stock_quantity = ?
                WHERE medicine_id = ?
                """;

        if (medicine.getName() == null
                || medicine.getName().isBlank()
                || medicine.getUnitPrice() == null
                || medicine.getUnitPrice().signum() < 0
                || medicine.getStockQuantity() < 0) {
            System.out.println("Invalid medicine details.");
            return false;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, medicine.getName().trim());
            ps.setBigDecimal(2, medicine.getUnitPrice());
            ps.setInt(3, medicine.getStockQuantity());
            ps.setInt(4, medicine.getMedicineId());

            return ps.executeUpdate() > 0;
        }
    }

    // View medicines with low stock
    public void viewLowStockMedicines(int threshold)
            throws SQLException {

        if (threshold < 0) {
            System.out.println("Threshold cannot be negative.");
            return;
        }

        String sql = """
                SELECT medicine_id, name, unit_price, stock_quantity
                FROM medicines
                WHERE stock_quantity <= ?
                ORDER BY stock_quantity, name
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, threshold);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n===== LOW-STOCK MEDICINES =====");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    new Medicine(
                            rs.getInt("medicine_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("unit_price"),
                            rs.getInt("stock_quantity")
                    ).displayDetails();
                }

                if (!found) {
                    System.out.println(
                            "No medicines at or below the stock threshold."
                    );
                }
            }
        }
    }

    
    // ==================== PRESCRIPTION MANAGEMENT ====================

    // Create a prescription and return its generated ID
    public int createPrescription(Prescription prescription)
            throws SQLException {

        if (prescription == null
                || prescription.getRecordId() <= 0
                || prescription.getPrescriptionDate() == null
                || prescription.getInstructions() == null
                || prescription.getInstructions().isBlank()) {
            System.out.println("Invalid prescription details.");
            return -1;
        }

        if (prescription.getPrescriptionDate()
                .isAfter(LocalDate.now())) {
            System.out.println(
                    "Prescription date cannot be in the future.");
            return -1;
        }

        String sql = """
                INSERT INTO prescriptions
                    (record_id, prescription_date, instructions)
                SELECT record_id, ?, ?
                FROM medical_records
                WHERE record_id = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, java.sql.Date.valueOf(
                    prescription.getPrescriptionDate()));
            ps.setString(2, prescription.getInstructions().trim());
            ps.setInt(3, prescription.getRecordId());

            if (ps.executeUpdate() == 0) {
                System.out.println(
                        "Medical record not found. Prescription not created.");
                return -1;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int prescriptionId = keys.getInt(1);
                    System.out.println(
                            "Prescription created successfully. ID: "
                                    + prescriptionId);
                    return prescriptionId;
                }
            }
        }

        return -1;
    }

    // Add a medicine to an existing prescription
    
    // Add a medicine to a prescription and dispense it immediately.
    // Prescription item, medicine stock, and dispensed quantity
    // are updated in one database transaction.
    public boolean addPrescriptionItem(
            int prescriptionId,
            int medicineId,
            String dosage,
            String duration,
            int quantity) throws SQLException {

        if (prescriptionId <= 0 || medicineId <= 0
                || dosage == null || dosage.isBlank()
                || duration == null || duration.isBlank()
                || quantity <= 0) {

            System.out.println("Invalid prescription item details.");
            return false;
        }

        String prescriptionCheckSql = """
                SELECT prescription_id
                FROM prescriptions
                WHERE prescription_id = ?
                """;

        String medicineCheckSql = """
                SELECT name, stock_quantity
                FROM medicines
                WHERE medicine_id = ?
                FOR UPDATE
                """;

        String insertSql = """
                INSERT INTO prescription_items
                    (prescription_id, medicine_id, dosage,
                     duration, quantity, dispensed_quantity)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String updateStockSql = """
                UPDATE medicines
                SET stock_quantity = stock_quantity - ?
                WHERE medicine_id = ?
                  AND stock_quantity >= ?
                """;

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                // 1. Verify the prescription exists.
                try (PreparedStatement ps =
                             con.prepareStatement(prescriptionCheckSql)) {

                    ps.setInt(1, prescriptionId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Prescription not found.");
                        }
                    }
                }

                // 2. Lock the medicine row and check available stock.
                String medicineName;
                int availableStock;

                try (PreparedStatement ps =
                             con.prepareStatement(medicineCheckSql)) {

                    ps.setInt(1, medicineId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Medicine not found.");
                        }

                        medicineName = rs.getString("name");
                        availableStock = rs.getInt("stock_quantity");
                    }
                }

                if (availableStock < quantity) {
                    con.rollback();

                    System.out.println(
                            "Insufficient stock for " + medicineName
                            + ". Available: " + availableStock
                            + ", requested: " + quantity);

                    return false;
                }

                // 3. Add the prescription item.
                // Since it is dispensed immediately, the initial
                // dispensed_quantity equals the prescribed quantity.
                try (PreparedStatement ps =
                             con.prepareStatement(insertSql)) {

                    ps.setInt(1, prescriptionId);
                    ps.setInt(2, medicineId);
                    ps.setString(3, dosage.trim());
                    ps.setString(4, duration.trim());
                    ps.setInt(5, quantity);
                    ps.setInt(6, quantity);

                    ps.executeUpdate();
                }

                // 4. Deduct the dispensed quantity from inventory.
                try (PreparedStatement ps =
                             con.prepareStatement(updateStockSql)) {

                    ps.setInt(1, quantity);
                    ps.setInt(2, medicineId);
                    ps.setInt(3, quantity);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Stock update failed.");
                    }
                }

                // 5. Commit both changes together.
                con.commit();

                int remainingStock = availableStock - quantity;

                System.out.println(
                        "Medicine added to prescription successfully.");
                System.out.println(
                        "Medicine dispensed: " + medicineName);
                System.out.println(
                        "Quantity dispensed: " + quantity);
                System.out.println(
                        "Remaining stock: " + remainingStock);

                if (remainingStock == 0) {
                    System.out.println(
                            "WARNING: " + medicineName
                            + " is now OUT OF STOCK.");
                } else if (remainingStock <= 10) {
                    System.out.println(
                            "WARNING: Low stock for " + medicineName
                            + ". Only " + remainingStock
                            + " units remain.");
                }

                return true;

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;

            } finally {
                con.setAutoCommit(true);
            }

        }
    }


    // Return prescription IDs for a record, ordered newest first.
    // IDs stay internal; the console lets the user select by list number.
    public List<Integer> getPrescriptionIdsByRecord(int recordId)
            throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = """
                SELECT prescription_id
                FROM prescriptions
                WHERE record_id = ?
                ORDER BY prescription_date DESC, prescription_id DESC
                """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("prescription_id"));
                }
            }
        }
        return ids;
    }

    // View prescriptions and their medicines for a medical record
    public void viewPrescriptionsByRecord(int recordId)
            throws SQLException {

        String sql = """
                SELECT p.prescription_id,
                       p.record_id,
                       p.prescription_date,
                       p.instructions,
                       pi.item_id,
                       m.name AS medicine_name,
                       m.unit_price,
                       pi.dosage,
                       pi.duration,
                       pi.quantity
                FROM prescriptions p
                LEFT JOIN prescription_items pi
                    ON p.prescription_id = pi.prescription_id
                LEFT JOIN medicines m
                    ON pi.medicine_id = m.medicine_id
                WHERE p.record_id = ?
                ORDER BY p.prescription_date DESC,
                         p.prescription_id DESC,
                         pi.item_id
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, recordId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                int previousPrescriptionId = -1;

                System.out.println(
                        "\n===== PRESCRIPTIONS FOR MEDICAL RECORD "
                                + recordId + " =====");

                while (rs.next()) {
                    found = true;

                    int currentPrescriptionId =
                            rs.getInt("prescription_id");

                    if (currentPrescriptionId != previousPrescriptionId) {
                        System.out.println(
                                "\nPrescription ID: "
                                        + currentPrescriptionId);
                        System.out.println(
                                "Date: "
                                        + rs.getDate("prescription_date"));
                        System.out.println(
                                "Instructions: "
                                        + rs.getString("instructions"));

                        previousPrescriptionId = currentPrescriptionId;
                    }

                    if (rs.getObject("item_id") != null) {
                        System.out.println(
                                "  Medicine: "
                                        + rs.getString("medicine_name"));
                        System.out.println(
                                "  Dosage: " + rs.getString("dosage"));
                        System.out.println(
                                "  Duration: " + rs.getString("duration"));
                        System.out.println(
                                "  Quantity: " + rs.getInt("quantity"));
                        System.out.println(
                                "  Unit Price: Rs. "
                                        + rs.getBigDecimal("unit_price"));
                    } else {
                        System.out.println(
                                "  No medicines added to this prescription.");
                    }
                }

                if (!found) {
                    System.out.println(
                            "No prescriptions found for this medical record.");
                }
            }
        }
    }


    
    // ==================== BILLING MANAGEMENT ====================

    public int createBill(int patientId, LocalDate billDate,
                          List<BillItem> items) throws SQLException {

        if (billDate == null || items == null || items.isEmpty()) {
            System.out.println("Bill date and at least one charge are required.");
            return -1;
        }

        if (billDate.isAfter(LocalDate.now())) {
            System.out.println("Bill date cannot be in the future.");
            return -1;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (BillItem item : items) {
            if (item == null
                    || item.getDescription() == null
                    || item.getDescription().isBlank()
                    || item.getAmount() == null
                    || item.getAmount().signum() < 0
                    || item.getAmount().scale() > 2) {
                System.out.println("Invalid bill item.");
                return -1;
            }

            total = total.add(item.getAmount());
        }

        String checkPatientSql =
                "SELECT patient_id FROM patients WHERE patient_id = ?";

        String insertBillSql = """
                INSERT INTO bills (patient_id, bill_date, total_amount, status)
                VALUES (?, ?, ?, 'UNPAID')
                """;

        String insertItemSql = """
                INSERT INTO bill_items (bill_id, description, amount)
                VALUES (?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps =
                             con.prepareStatement(checkPatientSql)) {
                    ps.setInt(1, patientId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Patient not found.");
                        }
                    }
                }

                int billId;

                try (PreparedStatement ps = con.prepareStatement(
                        insertBillSql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, patientId);
                    ps.setDate(2, java.sql.Date.valueOf(billDate));
                    ps.setBigDecimal(3, total);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Could not generate bill ID.");
                        }
                        billId = keys.getInt(1);
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(insertItemSql)) {
                    for (BillItem item : items) {
                        ps.setInt(1, billId);
                        ps.setString(2, item.getDescription().trim());
                        ps.setBigDecimal(3, item.getAmount());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                con.commit();
                System.out.println("Bill generated successfully.");
                System.out.println("Total amount: Rs. " + total);

                return billId;

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // ==================== PAYMENT MANAGEMENT ====================

    public boolean recordPayment(int billId, BigDecimal amount,
                                 String paymentMethod)
            throws SQLException {

        if (billId <= 0 || amount == null
                || amount.signum() <= 0 || amount.scale() > 2
                || paymentMethod == null || paymentMethod.isBlank()) {
            System.out.println("Invalid payment details.");
            return false;
        }

        String billSql = """
                SELECT total_amount, status
                FROM bills
                WHERE bill_id = ?
                FOR UPDATE
                """;

        String paidSql = """
                SELECT COALESCE(SUM(amount), 0) AS paid
                FROM payments
                WHERE bill_id = ? AND status = 'COMPLETED'
                """;

        String insertPaymentSql = """
                INSERT INTO payments
                    (bill_id, amount, payment_method, payment_date, status)
                VALUES (?, ?, ?, CURRENT_TIMESTAMP, 'COMPLETED')
                """;

        String updateBillSql = """
                UPDATE bills
                SET status = ?
                WHERE bill_id = ?
                """;

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                BigDecimal total;

                try (PreparedStatement ps =
                             con.prepareStatement(billSql)) {
                    ps.setInt(1, billId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("Bill not found.");
                            con.rollback();
                            return false;
                        }
                        total = rs.getBigDecimal("total_amount");
                    }
                }

                BigDecimal alreadyPaid;

                try (PreparedStatement ps =
                             con.prepareStatement(paidSql)) {
                    ps.setInt(1, billId);

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        alreadyPaid = rs.getBigDecimal("paid");
                    }
                }

                BigDecimal balance = total.subtract(alreadyPaid);

                if (amount.compareTo(balance) > 0) {
                    System.out.println(
                            "Payment exceeds the outstanding balance of Rs. "
                                    + balance);
                    con.rollback();
                    return false;
                }

                try (PreparedStatement ps =
                             con.prepareStatement(insertPaymentSql)) {
                    ps.setInt(1, billId);
                    ps.setBigDecimal(2, amount);
                    ps.setString(3, paymentMethod.trim().toUpperCase());
                    ps.executeUpdate();
                }

                BigDecimal newPaid = alreadyPaid.add(amount);
                String newStatus = newPaid.compareTo(total) >= 0
                        ? "PAID"
                        : "PARTIALLY_PAID";

                try (PreparedStatement ps =
                             con.prepareStatement(updateBillSql)) {
                    ps.setString(1, newStatus);
                    ps.setInt(2, billId);
                    ps.executeUpdate();
                }

                con.commit();

                System.out.println("Payment recorded successfully.");
                System.out.println("Total bill: Rs. " + total);
                System.out.println("Total paid: Rs. " + newPaid);
                System.out.println(
                        "Outstanding balance: Rs. " + total.subtract(newPaid));
                System.out.println("Bill status: " + newStatus);

                return true;

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // View billing history for a selected patient.
    public void viewBillingHistory(int patientId) throws SQLException {

        String sql = """
                SELECT b.bill_id, b.bill_date, b.total_amount, b.status,
                       COALESCE(
                           (SELECT SUM(p.amount)
                            FROM payments p
                            WHERE p.bill_id = b.bill_id
                              AND p.status = 'COMPLETED'), 0
                       ) AS paid_amount
                FROM bills b
                WHERE b.patient_id = ?
                ORDER BY b.bill_date DESC, b.bill_id DESC
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;

                    BigDecimal total = rs.getBigDecimal("total_amount");
                    BigDecimal paid = rs.getBigDecimal("paid_amount");
                    BigDecimal balance = total.subtract(paid);

                    System.out.println("\n------------------------------");
                    System.out.println("Bill ID: " + rs.getInt("bill_id"));
                    System.out.println("Date: " + rs.getDate("bill_date"));
                    System.out.println("Total: Rs. " + total);
                    System.out.println("Paid: Rs. " + paid);
                    System.out.println("Balance: Rs. " + balance);
                    System.out.println("Status: " + rs.getString("status"));
                }

                if (!found) {
                    System.out.println("No bills found for this patient.");
                }
            }
        }
    }

    // View all unpaid or partially paid bills.
    public void viewPendingBills() throws SQLException {

        String sql = """
                SELECT b.bill_id, p.name AS patient_name, b.bill_date,
                       b.total_amount, b.status,
                       COALESCE(
                           (SELECT SUM(pay.amount)
                            FROM payments pay
                            WHERE pay.bill_id = b.bill_id
                              AND pay.status = 'COMPLETED'), 0
                       ) AS paid_amount
                FROM bills b
                JOIN patients p ON p.patient_id = b.patient_id
                WHERE b.status IN ('UNPAID', 'PARTIALLY_PAID')
                ORDER BY b.bill_date, b.bill_id
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                BigDecimal total = rs.getBigDecimal("total_amount");
                BigDecimal paid = rs.getBigDecimal("paid_amount");

                System.out.println("\n------------------------------");
                System.out.println("Patient: " + rs.getString("patient_name"));
                System.out.println("Bill ID: " + rs.getInt("bill_id"));
                System.out.println("Date: " + rs.getDate("bill_date"));
                System.out.println("Total: Rs. " + total);
                System.out.println("Paid: Rs. " + paid);
                System.out.println("Balance: Rs. " + total.subtract(paid));
                System.out.println("Status: " + rs.getString("status"));
            }

            if (!found) {
                System.out.println("No pending bills found.");
            }
        }
    }

    // Display a bill and its charge items.
    public void viewBillDetails(int billId) throws SQLException {

        String billSql = """
                SELECT b.bill_id, p.name AS patient_name, b.bill_date,
                       b.total_amount, b.status
                FROM bills b
                JOIN patients p ON p.patient_id = b.patient_id
                WHERE b.bill_id = ?
                """;

        String itemsSql = """
                SELECT description, amount
                FROM bill_items
                WHERE bill_id = ?
                ORDER BY bill_item_id
                """;

        try (Connection con = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(billSql)) {
                ps.setInt(1, billId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Bill not found.");
                        return;
                    }

                    System.out.println("\n========== BILL DETAILS ==========");
                    System.out.println("Patient: " + rs.getString("patient_name"));
                    System.out.println("Bill date: " + rs.getDate("bill_date"));
                    System.out.println("Total: Rs. " + rs.getBigDecimal("total_amount"));
                    System.out.println("Status: " + rs.getString("status"));
                }
            }

            try (PreparedStatement ps = con.prepareStatement(itemsSql)) {
                ps.setInt(1, billId);

                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("\nCharges:");

                    while (rs.next()) {
                        System.out.println(
                                rs.getString("description") + " - Rs. "
                                        + rs.getBigDecimal("amount"));
                    }
                }
            }
        }
    }

    
    // ==================== MEDICINE DISPENSING ====================

    public boolean dispenseMedicine(
            int prescriptionItemId, int quantityToDispense)
            throws SQLException {

        if (prescriptionItemId <= 0 || quantityToDispense <= 0) {
            System.out.println("Invalid dispensing details.");
            return false;
        }

        String itemSql = """
                SELECT pi.medicine_id, pi.quantity, pi.dispensed_quantity,
                       m.name, m.stock_quantity
                FROM prescription_items pi
                JOIN medicines m ON m.medicine_id = pi.medicine_id
                WHERE pi.item_id = ?
                FOR UPDATE
                """;

        String stockSql = """
                UPDATE medicines
                SET stock_quantity = stock_quantity - ?
                WHERE medicine_id = ?
                  AND stock_quantity >= ?
                """;

        String dispensedSql = """
                UPDATE prescription_items
                SET dispensed_quantity = dispensed_quantity + ?
                WHERE item_id = ?
                  AND dispensed_quantity + ? <= quantity
                """;

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                int medicineId;
                int prescribed;
                int alreadyDispensed;
                int available;
                String medicineName;

                try (PreparedStatement ps =
                             con.prepareStatement(itemSql)) {
                    ps.setInt(1, prescriptionItemId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println(
                                    "Prescription medicine not found.");
                            con.rollback();
                            return false;
                        }

                        medicineId = rs.getInt("medicine_id");
                        prescribed = rs.getInt("quantity");
                        alreadyDispensed =
                                rs.getInt("dispensed_quantity");
                        available = rs.getInt("stock_quantity");
                        medicineName = rs.getString("name");
                    }
                }

                int remaining = prescribed - alreadyDispensed;

                if (quantityToDispense > remaining) {
                    System.out.println(
                            "Cannot dispense that quantity. "
                            + "Remaining prescribed quantity: " + remaining);
                    con.rollback();
                    return false;
                }

                if (quantityToDispense > available) {
                    System.out.println(
                            "Insufficient stock for " + medicineName
                            + ". Available: " + available);
                    con.rollback();
                    return false;
                }

                try (PreparedStatement ps =
                             con.prepareStatement(stockSql)) {
                    ps.setInt(1, quantityToDispense);
                    ps.setInt(2, medicineId);
                    ps.setInt(3, quantityToDispense);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Stock update failed. Please retry.");
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(dispensedSql)) {
                    ps.setInt(1, quantityToDispense);
                    ps.setInt(2, prescriptionItemId);
                    ps.setInt(3, quantityToDispense);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Dispensed quantity update failed.");
                    }
                }

                con.commit();

                int remainingStock = available - quantityToDispense;

                System.out.println("Medicine dispensed successfully.");
                System.out.println("Medicine: " + medicineName);
                System.out.println("Quantity dispensed: "
                        + quantityToDispense);
                System.out.println("Remaining stock: " + remainingStock);

                if (remainingStock == 0) {
                    System.out.println(
                            "WARNING: This medicine is now OUT OF STOCK.");
                } else if (remainingStock <= 10) {
                    System.out.println(
                            "WARNING: Low stock! Only "
                            + remainingStock + " units remain.");
                }

                return true;

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

public int createPrescriptionWithItems(
        Prescription prescription,
        List<PrescriptionItem> items) throws SQLException {

    if (prescription == null
            || prescription.getRecordId() <= 0
            || prescription.getPrescriptionDate() == null
            || prescription.getPrescriptionDate().isAfter(LocalDate.now())
            || prescription.getInstructions() == null
            || prescription.getInstructions().isBlank()
            || items == null
            || items.isEmpty()) {

        System.out.println(
                "Prescription details and at least one medicine are required.");
        return -1;
    }

    String prescriptionSql = """
            INSERT INTO prescriptions
                (record_id, prescription_date, instructions)
            SELECT record_id, ?, ?
            FROM medical_records
            WHERE record_id = ?
            """;

    String medicineSql = """
            SELECT name, stock_quantity
            FROM medicines
            WHERE medicine_id = ?
            FOR UPDATE
            """;

    String insertItemSql = """
            INSERT INTO prescription_items
                (prescription_id, medicine_id, dosage, duration,
                 quantity, dispensed_quantity)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    String updateStockSql = """
            UPDATE medicines
            SET stock_quantity = stock_quantity - ?
            WHERE medicine_id = ?
              AND stock_quantity >= ?
            """;

    try (Connection con = DatabaseConnection.getConnection()) {
        con.setAutoCommit(false);

        try {
            int prescriptionId;

            // 1. Create the prescription header.
            try (PreparedStatement ps = con.prepareStatement(
                    prescriptionSql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setDate(1, java.sql.Date.valueOf(
                        prescription.getPrescriptionDate()));
                ps.setString(2,
                        prescription.getInstructions().trim());
                ps.setInt(3, prescription.getRecordId());

                if (ps.executeUpdate() != 1) {
                    throw new SQLException(
                            "Medical record not found.");
                }

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException(
                                "Could not generate prescription ID.");
                    }

                    prescriptionId = keys.getInt(1);
                }
            }

            // 2. Validate and save each medicine.
            for (PrescriptionItem item : items) {
                if (item == null
                        || item.getMedicineId() <= 0
                        || item.getDosage() == null
                        || item.getDosage().isBlank()
                        || item.getDuration() == null
                        || item.getDuration().isBlank()
                        || item.getQuantity() <= 0) {

                    throw new SQLException(
                            "Invalid medicine details.");
                }

                String medicineName;
                int availableStock;

                // Lock the medicine row so concurrent transactions
                // cannot consume the same stock.
                try (PreparedStatement ps =
                             con.prepareStatement(medicineSql)) {

                    ps.setInt(1, item.getMedicineId());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "A selected medicine no longer exists.");
                        }

                        medicineName = rs.getString("name");
                        availableStock =
                                rs.getInt("stock_quantity");
                    }
                }

                if (item.getQuantity() > availableStock) {
                    throw new SQLException(
                            "Insufficient stock for " + medicineName
                            + ". Available: " + availableStock
                            + ", required: " + item.getQuantity());
                }

                // 3. Insert the medicine and mark it as dispensed.
                try (PreparedStatement ps =
                             con.prepareStatement(insertItemSql)) {

                    ps.setInt(1, prescriptionId);
                    ps.setInt(2, item.getMedicineId());
                    ps.setString(3, item.getDosage().trim());
                    ps.setString(4, item.getDuration().trim());
                    ps.setInt(5, item.getQuantity());

                    // The medicine is dispensed as part of this workflow.
                    ps.setInt(6, item.getQuantity());

                    ps.executeUpdate();
                }

                // 4. Deduct the quantity from inventory.
                try (PreparedStatement ps =
                             con.prepareStatement(updateStockSql)) {

                    ps.setInt(1, item.getQuantity());
                    ps.setInt(2, item.getMedicineId());
                    ps.setInt(3, item.getQuantity());

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Stock update failed for "
                                + medicineName + ".");
                    }
                }
            }

            // All operations succeeded.
            con.commit();

            System.out.println(
                    "Prescription and medicines saved successfully.");
            System.out.println(
                    "Medicine inventory updated successfully.");

            return prescriptionId;

        } catch (SQLException | RuntimeException e) {
            con.rollback();
            System.out.println(
                    "Prescription creation failed: " + e.getMessage());
            System.out.println(
                    "All changes have been rolled back.");
            return -1;

        } finally {
            con.setAutoCommit(true);
        }
    }
}
}
