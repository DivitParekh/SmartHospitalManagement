
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final HospitalService hospitalService =
            new HospitalService();

    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("ddMMuuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final DateTimeFormatter INPUT_TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private static final DateTimeFormatter DISPLAY_TIME_FORMAT =
            DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    public static void main(String[] args) {

        int choice = -1;

        try {
            while (choice != 0) {
                displayMainMenu();
                choice = readInt();

                try {
                    switch (choice) {
                        case 1 -> registerPatient();
                        case 2 -> viewAllPatients();
                        case 3 -> searchPatient();
                        case 4 -> updatePatient();
                        case 5 -> registerDoctor();
                        case 6 -> viewAllDoctors();
                        case 7 -> searchDoctors();
                        case 8 -> scheduleAppointment();
                        case 9 -> viewAllAppointments();
                        case 10 -> updateAppointmentStatus();
                        case 11 -> addMedicalRecord();
                        // case 12 -> viewAllMedicalRecords();
                        case 12 -> viewMedicalRecordsByPatient();
                        case 13 -> addTreatment();
                        case 14 -> viewAllTreatments();
                        case 15 -> viewTreatmentsByRecord();
                        case 16 -> addMedicine();
                        case 17 -> hospitalService.viewAllMedicines();
                        case 18 ->searchMedicine();
                        case 19 ->updateMedicine();
                        case 20 -> viewLowStockMedicines();
                        case 21 -> createPrescription();
                        case 22 -> viewPrescriptionsByRecord();
                        case 23 -> generatePatientBill();
                        case 24 -> recordPatientPayment();
                        case 25 -> viewPatientBillingHistory();
                        case 26 -> hospitalService.viewPendingBills();
                        case 27 -> viewSelectedBillDetails();
                        case 0 -> System.out.println("Goodbye!");
                        default -> System.out.println(
                                "Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println(
                            "Database error: " + e.getMessage());
                }

                if (choice != 0) {
                    System.out.println();
                }
            }
        } finally {
            scanner.close();
        }
    }

    // ==================== MAIN MENU ====================

    private static void displayMainMenu() {
       
        System.out.println("\n========== SMART HOSPITAL MANAGEMENT SYSTEM ==========");
        System.out.println("1. Register Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. Search Patient by Name");
        System.out.println("4. Update Patient Details");
        System.out.println("5. Register Doctor");
        System.out.println("6. View All Doctors");
        System.out.println("7. Search Doctor by Specialization");
        System.out.println("8. Schedule Appointment");
        System.out.println("9. View All Appointments");
        System.out.println("10. Update Appointment Status");
        System.out.println("11. Add Medical Record / Diagnosis");
        System.out.println("12. View Medical Records by Patient");
        System.out.println("13. Add Treatment");
        System.out.println("14. View All Treatments");
        System.out.println("15. View Treatments by Patient / Diagnosis");
        System.out.println("16. Add Medicine");
        System.out.println("17. View All Medicines");
        System.out.println("18. Search Medicine by Name");
        System.out.println("19. Update Medicine Details");
        System.out.println("20. View Low-Stock Medicines");
        System.out.println("21. Create Prescription (Including Medicines)");
        System.out.println("22. View Prescriptions by Patient");
        System.out.println("23. Generate Patient Bill");
        System.out.println("24. Record Payment");
        System.out.println("25. View Patient Billing History");
        System.out.println("26. View Pending Bills");
        System.out.println("27. View Bill Details");
        System.out.println("0. Exit");
        System.out.println("======================================================");
        System.out.print("Enter your choice: ");

    }

    // ==================== INPUT HELPERS ====================

    private static int readInt() {
        while (true) {
            if (!scanner.hasNextLine()) {
                return 0;
            }

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);

        if (!scanner.hasNextLine()) {
            return null;
        }

        return scanner.nextLine().trim();
    }

    // ==================== PATIENT MANAGEMENT ====================

    private static void registerPatient() throws SQLException {
        System.out.println("\n--- Register Patient ---");

        String name = readLine("Name: ");
        if (name == null) return;

        System.out.print("Age: ");
        int age = readInt();

        String phone = readLine("Phone: ");
        if (phone == null) return;

        String gender = readLine("Gender: ");
        if (gender == null) return;

        String address = readLine("Address: ");
        if (address == null) return;

        String bloodGroup = readLine("Blood group: ");
        if (bloodGroup == null) return;

        if (name.isEmpty() || age < 1 || age > 120) {
            System.out.println("Invalid name or age.");
            return;
        }

        Patient patient = new Patient(
                0, name, age, phone, gender, address, bloodGroup);

        if (hospitalService.registerPatient(patient)) {
            System.out.println("Patient registered successfully!");
        } else {
            System.out.println("Patient registration failed.");
        }
    }

    private static void viewAllPatients() throws SQLException {
        List<Patient> patients = hospitalService.getAllPatients();

        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }

        System.out.println("\n--- Patient Records ---");

        for (Patient patient : patients) {
            patient.displayDetails();
            System.out.println("-----------------------");
        }
    }

    private static void searchPatient() throws SQLException {
        String name = readLine("Enter patient name to search: ");
        if (name == null) return;

        if (name.isEmpty()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        List<Patient> patients =
                hospitalService.searchPatientsByName(name);

        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        System.out.println("\n--- Search Results ---");

        for (Patient patient : patients) {
            patient.displayDetails();
            System.out.println("-----------------------");
        }
    }

    private static void updatePatient() throws SQLException {
        String name = readLine("Enter patient name to find: ");
        if (name == null || name.isBlank()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        List<Patient> patients = hospitalService.searchPatientsByName(name);
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        System.out.println("\nMatching Patients:");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            System.out.println((i + 1) + ". " + p.getName()
                    + " | Age: " + p.getAge()
                    + " | Phone: " + p.getPhone());
        }

        System.out.print("Select patient number to update: ");
        int choice = readInt();
        if (choice < 1 || choice > patients.size()) {
            System.out.println("Invalid patient selection.");
            return;
        }

        Patient patient = patients.get(choice - 1);
        System.out.println("Updating: " + patient.getName());
        System.out.println("Press Enter to keep an existing value.");

        String value = readLine("Name [" + patient.getName() + "]: ");
        if (value == null) return;
        if (!value.isEmpty()) patient.setName(value);

        value = readLine("Age [" + patient.getAge() + "]: ");
        if (value == null) return;
        if (!value.isEmpty()) {
            try {
                int age = Integer.parseInt(value);
                if (age < 1 || age > 120) {
                    System.out.println("Invalid age. Update cancelled.");
                    return;
                }
                patient.setAge(age);
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Update cancelled.");
                return;
            }
        }

        value = readLine("Phone [" + patient.getPhone() + "]: ");
        if (value == null) return;
        if (!value.isEmpty()) patient.setPhone(value);

        value = readLine("Gender [" + patient.getGender() + "]: ");
        if (value == null) return;
        if (!value.isEmpty()) patient.setGender(value);

        value = readLine("Address [" + patient.getAddress() + "]: ");
        if (value == null) return;
        if (!value.isEmpty()) patient.setAddress(value);

        value = readLine("Blood group [" + patient.getBloodGroup() + "]: ");
        if (value == null) return;
        if (!value.isEmpty()) patient.setBloodGroup(value);

        if (hospitalService.updatePatient(patient)) {
            System.out.println("Patient updated successfully!");
        } else {
            System.out.println("No patient record was updated.");
        }
    }

    // ==================== DOCTOR MANAGEMENT ====================

    private static void registerDoctor() throws SQLException {
        System.out.println("\n--- Register Doctor ---");

        String name = readLine("Name: ");
        if (name == null) return;

        System.out.print("Age: ");
        int age = readInt();

        String phone = readLine("Phone: ");
        if (phone == null) return;

        String specialization = readLine("Specialization: ");
        if (specialization == null) return;

        if (name.isEmpty() || specialization.isEmpty()
                || age < 1 || age > 120) {
            System.out.println(
                    "Invalid name, specialization, or age.");
            return;
        }

        Doctor doctor = new Doctor(
                0, name, age, phone, specialization, true);

        if (hospitalService.registerDoctor(doctor)) {
            System.out.println("Doctor registered successfully!");
        } else {
            System.out.println("Doctor registration failed.");
        }
    }

    private static void viewAllDoctors() throws SQLException {
        List<Doctor> doctors = hospitalService.getAllDoctors();

        if (doctors.isEmpty()) {
            System.out.println("No doctors registered yet.");
            return;
        }

        System.out.println("\n--- Doctor Records ---");

        for (Doctor doctor : doctors) {
            doctor.displayDetails();
            System.out.println("-----------------------");
        }
    }

    private static void searchDoctors() throws SQLException {
        String specialization =
                readLine("Enter specialization to search: ");
        if (specialization == null) return;

        if (specialization.isEmpty()) {
            System.out.println("Specialization cannot be empty.");
            return;
        }

        List<Doctor> doctors =
                hospitalService.searchDoctorsBySpecialization(
                        specialization);

        if (doctors.isEmpty()) {
            System.out.println(
                    "No doctors found for that specialization.");
            return;
        }

        System.out.println("\n--- Search Results ---");

        for (Doctor doctor : doctors) {
            doctor.displayDetails();
            System.out.println("-----------------------");
        }
    }

    // ==================== APPOINTMENT MANAGEMENT ====================

    private static void scheduleAppointment() throws SQLException {
        System.out.println("\n===== SCHEDULE APPOINTMENT =====");

        String patientName = readLine("Enter Patient Name: ");
        if (patientName == null) return;

        if (patientName.isEmpty()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        List<Patient> patients =
                hospitalService.searchPatientsByName(patientName);

        if (patients.isEmpty()) {
            System.out.println("No matching patients found.");
            return;
        }

        System.out.println("\nMatching Patients:");

        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);

            System.out.println(
                    (i + 1) + ". " + p.getName()
                    + " | Age: " + p.getAge()
                    + " | Phone: " + p.getPhone());
        }

        System.out.print("Select Patient (enter number): ");
        int patientChoice = readInt();

        if (patientChoice < 1 || patientChoice > patients.size()) {
            System.out.println("Invalid patient selection.");
            return;
        }

        Patient selectedPatient = patients.get(patientChoice - 1);

        String doctorName = readLine("\nEnter Doctor Name: ");
        if (doctorName == null) return;

        if (doctorName.isEmpty()) {
            System.out.println("Doctor name cannot be empty.");
            return;
        }

        List<Doctor> doctors =
                hospitalService.searchDoctorsByName(doctorName);

        if (doctors.isEmpty()) {
            System.out.println("No matching doctors found.");
            return;
        }

        System.out.println("\nMatching Doctors:");

        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);

            System.out.println(
                    (i + 1) + ". Dr. " + d.getName()
                    + " | Specialization: " + d.getSpecialization()
                    + " | " + (d.isAvailable()
                            ? "Available" : "Unavailable"));
        }

        System.out.print("Select Doctor (enter number): ");
        int doctorChoice = readInt();

        if (doctorChoice < 1 || doctorChoice > doctors.size()) {
            System.out.println("Invalid doctor selection.");
            return;
        }

        Doctor selectedDoctor = doctors.get(doctorChoice - 1);

        if (!selectedDoctor.isAvailable()) {
            System.out.println("This doctor is currently unavailable.");
            return;
        }

        String dateInput =
                readLine("\nAppointment Date (DDMMYYYY): ");
        if (dateInput == null) return;

        String timeInput =
                readLine("Appointment Time (HH:MM AM/PM): ");
        if (timeInput == null) return;

        String reason = readLine("Reason for Appointment: ");
        if (reason == null) return;

        if (reason.isEmpty()) {
            System.out.println("Reason cannot be empty.");
            return;
        }

        try {
            LocalDate date =
                    LocalDate.parse(dateInput, INPUT_DATE_FORMAT);

            LocalTime time = LocalTime.parse(
                    timeInput.toUpperCase(Locale.ENGLISH),
                    INPUT_TIME_FORMAT);

            if (date.isBefore(LocalDate.now())) {
                System.out.println(
                        "Appointment date cannot be in the past.");
                return;
            }

            if (date.equals(LocalDate.now())
                    && time.isBefore(LocalTime.now())) {
                System.out.println(
                        "Appointment time cannot be in the past.");
                return;
            }

            System.out.println("\n===== CONFIRM APPOINTMENT =====");
            System.out.println("Patient: " + selectedPatient.getName());
            System.out.println("Doctor: Dr. " + selectedDoctor.getName());
            System.out.println(
                    "Specialization: " + selectedDoctor.getSpecialization());
            System.out.println(
                    "Date: " + date.format(DISPLAY_DATE_FORMAT));
            System.out.println(
                    "Time: " + time.format(DISPLAY_TIME_FORMAT));
            System.out.println("Reason: " + reason);

            String confirmation =
                    readLine("\nConfirm Appointment? (yes/no): ");
            if (confirmation == null) return;

            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Appointment booking cancelled.");
                return;
            }

            Appointment appointment = new Appointment(
                    selectedPatient.getId(),
                    selectedDoctor.getId(),
                    date,
                    time,
                    reason);

            if (hospitalService.scheduleAppointment(appointment)) {
                System.out.println(
                        "\nAppointment scheduled successfully!");
            } else {
                System.out.println(
                        "\nAppointment could not be scheduled.");
            }

        } catch (DateTimeParseException e) {
            System.out.println(
                    "Invalid date or time. Use DDMMYYYY and "
                    + "HH:MM AM/PM, for example 15112026 and 10:30 PM.");
        }
    }

    private static void viewAllAppointments() throws SQLException {
        System.out.println("\n===== ALL APPOINTMENTS =====");
        hospitalService.viewAllAppointments();
    }

    private static void updateAppointmentStatus() throws SQLException {
        System.out.println("\n===== UPDATE APPOINTMENT STATUS =====");

        hospitalService.viewAllAppointments();

        System.out.print("\nEnter Appointment ID to update: ");
        int appointmentId = readInt();

        System.out.println("\nSelect New Status:");
        System.out.println("1. Completed");
        System.out.println("2. Missed");
        System.out.println("3. Cancelled");
        System.out.print("Enter choice: ");

        int choice = readInt();
        String newStatus;

        switch (choice) {
            case 1 -> newStatus = "COMPLETED";
            case 2 -> newStatus = "MISSED";
            case 3 -> newStatus = "CANCELLED";
            default -> {
                System.out.println("Invalid status choice.");
                return;
            }
        }

        String confirmation = readLine(
                "Confirm status change to " + newStatus + "? (yes/no): ");
        if (confirmation == null) return;

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Status update cancelled.");
            return;
        }

        if (hospitalService.updateAppointmentStatus(
                appointmentId, newStatus)) {
            System.out.println(
                    "Appointment status updated successfully!");
        } else {
            System.out.println(
                    "Status was not updated. Check the appointment ID; "
                    + "it may not exist or may no longer be scheduled.");
        }
    }

    // ==================== MEDICAL RECORD MANAGEMENT ====================

    private static void addMedicalRecord() throws SQLException {
        System.out.println("\n===== ADD MEDICAL RECORD =====");

        String patientName = readLine("Enter patient name to search: ");
        if (patientName == null || patientName.isBlank()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        List<Patient> patients = hospitalService.searchPatientsByName(patientName);
        if (patients.isEmpty()) {
            System.out.println("No matching patients found.");
            return;
        }

        System.out.println("\nMatching Patients:");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            System.out.println((i + 1) + ". " + p.getName()
                    + " | Age: " + p.getAge() + " | Phone: " + p.getPhone());
        }
        System.out.print("Select patient number: ");
        int patientChoice = readInt();
        if (patientChoice < 1 || patientChoice > patients.size()) {
            System.out.println("Invalid patient selection.");
            return;
        }
        Patient selectedPatient = patients.get(patientChoice - 1);

        String doctorName = readLine("Enter doctor name to search: ");
        if (doctorName == null || doctorName.isBlank()) {
            System.out.println("Doctor name cannot be empty.");
            return;
        }
        List<Doctor> doctors = hospitalService.searchDoctorsByName(doctorName);
        if (doctors.isEmpty()) {
            System.out.println("No matching doctors found.");
            return;
        }

        System.out.println("\nMatching Doctors:");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            System.out.println((i + 1) + ". Dr. " + d.getName()
                    + " | " + d.getSpecialization()
                    + " | " + (d.isAvailable() ? "Available" : "Unavailable"));
        }
        System.out.print("Select doctor number: ");
        int doctorChoice = readInt();
        if (doctorChoice < 1 || doctorChoice > doctors.size()) {
            System.out.println("Invalid doctor selection.");
            return;
        }
        Doctor selectedDoctor = doctors.get(doctorChoice - 1);

        String diagnosis = readLine("Enter diagnosis: ");
        if (diagnosis == null || diagnosis.isBlank()) {
            System.out.println("Diagnosis cannot be empty.");
            return;
        }

        String dateInput = readLine("Enter record date (DDMMYYYY): ");
        if (dateInput == null) return;

        LocalDate recordDate;
        try {
            recordDate = LocalDate.parse(dateInput, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use DDMMYYYY, for example 25102026.");
            return;
        }
        if (recordDate.isAfter(LocalDate.now())) {
            System.out.println("Record date cannot be in the future.");
            return;
        }

        System.out.println("\n===== CONFIRM MEDICAL RECORD =====");
        System.out.println("Patient: " + selectedPatient.getName());
        System.out.println("Doctor: Dr. " + selectedDoctor.getName());
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("Record Date: " + recordDate.format(DISPLAY_DATE_FORMAT));
        System.out.println("Appointment: Not linked");
        String confirm = readLine("Save this medical record? (Y/N): ");
        if (confirm == null || !confirm.equalsIgnoreCase("Y")) {
            System.out.println("Medical record not saved.");
            return;
        }

        MedicalRecord record = new MedicalRecord(
                selectedPatient.getId(), selectedDoctor.getId(), null,
                diagnosis.trim(), recordDate);

        if (hospitalService.addMedicalRecord(record)) {
            System.out.println("Medical record added successfully.");
        } else {
            System.out.println("Could not add the medical record.");
        }
    }

    private static void viewAllMedicalRecords() throws SQLException {
        System.out.println("\n===== ALL MEDICAL RECORDS =====");
        hospitalService.viewAllMedicalRecords();
    }

    private static void viewMedicalRecordsByPatient() throws SQLException {
        System.out.println("\n===== PATIENT MEDICAL HISTORY =====");
        String name = readLine("Enter patient name to search: ");
        if (name == null || name.isBlank()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        List<Patient> patients = hospitalService.searchPatientsByName(name);
        if (patients.isEmpty()) {
            System.out.println("No matching patients found.");
            return;
        }

        System.out.println("\nMatching Patients:");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            System.out.println((i + 1) + ". " + p.getName()
                    + " | Age: " + p.getAge() + " | Phone: " + p.getPhone());
        }

        System.out.print("Select patient number: ");
        int choice = readInt();
        if (choice < 1 || choice > patients.size()) {
            System.out.println("Invalid patient selection.");
            return;
        }

        Patient selected = patients.get(choice - 1);
        System.out.println("\nMedical history for " + selected.getName());
        hospitalService.viewMedicalRecordsByPatient(selected.getId());
    }

    
    // ==================== TREATMENT MANAGEMENT ====================

    
    private static void addTreatment() throws SQLException {
        System.out.println("\n===== ADD TREATMENT =====");

        // Step 1: Search for the patient
        String patientName = readLine("Enter patient name: ");

        if (patientName == null || patientName.isBlank()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        List<Patient> patients =
                hospitalService.searchPatientsByName(patientName);

        if (patients.isEmpty()) {
            System.out.println("No matching patients found.");
            return;
        }

        // Step 2: Select the patient
        System.out.println("\nMatching Patients:");

        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);

            System.out.println(
                    (i + 1) + ". " + patient.getName()
                    + " | Age: " + patient.getAge()
                    + " | Phone: " + patient.getPhone());
        }

        System.out.print("Select patient number: ");
        int patientChoice = readInt();

        if (patientChoice < 1 || patientChoice > patients.size()) {
            System.out.println("Invalid patient selection.");
            return;
        }

        Patient selectedPatient = patients.get(patientChoice - 1);

        // Step 3: Retrieve this patient's medical records
        List<MedicalRecord> records =
                hospitalService.getMedicalRecordsByPatient(
                        selectedPatient.getId());

        if (records.isEmpty()) {
            System.out.println(
                    "This patient has no medical records yet.");
            System.out.println(
                    "Please add a medical record before adding treatment.");
            return;
        }

        // Step 4: Select the medical record
        System.out.println(
                "\nMedical Records for "
                + selectedPatient.getName() + ":");

        DateTimeFormatter recordDateFormat =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (int i = 0; i < records.size(); i++) {
            MedicalRecord record = records.get(i);

            System.out.println(
                    (i + 1) + ". Diagnosis: " + record.getDiagnosis()
                    + " | Date: "
                    + record.getRecordDate().format(recordDateFormat));
        }

        System.out.print("Select medical record number: ");
        int recordChoice = readInt();

        if (recordChoice < 1 || recordChoice > records.size()) {
            System.out.println("Invalid medical record selection.");
            return;
        }

        MedicalRecord selectedRecord = records.get(recordChoice - 1);

        // Step 5: Enter treatment details
        String description =
                readLine("\nEnter treatment description: ");

        if (description == null || description.isBlank()) {
            System.out.println(
                    "Treatment description cannot be empty.");
            return;
        }

        String costInput =
                readLine("Enter treatment cost (Rs.): ");

        if (costInput == null || costInput.isBlank()) {
            System.out.println("Treatment cost is required.");
            return;
        }

        java.math.BigDecimal cost;

        try {
            cost = new java.math.BigDecimal(costInput);

            if (cost.signum() < 0) {
                System.out.println("Cost cannot be negative.");
                return;
            }

            cost = cost.setScale(
                    2, java.math.RoundingMode.UNNECESSARY);

        } catch (NumberFormatException
                 | ArithmeticException e) {
            System.out.println(
                    "Enter a valid cost with up to two decimal places.");
            return;
        }

        // Step 6: Confirm before saving
        System.out.println("\n===== CONFIRM TREATMENT =====");
        System.out.println("Patient: " + selectedPatient.getName());
        System.out.println("Diagnosis: " + selectedRecord.getDiagnosis());
        System.out.println("Treatment: " + description);
        System.out.println("Cost: Rs. " + cost);

        String confirmation =
                readLine("Save this treatment? (Y/N): ");

        if (confirmation == null
                || !confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Treatment addition cancelled.");
            return;
        }

        // The selected record ID is used internally
        Treatment treatment = new Treatment(
                selectedRecord.getRecordId(),
                description.trim(),
                cost);

        if (hospitalService.addTreatment(treatment)) {
            System.out.println("Treatment added successfully!");
        } else {
            System.out.println("Treatment could not be added.");
        }
    }


    private static void viewAllTreatments() throws SQLException {
        System.out.println("\n===== ALL TREATMENTS =====");
        hospitalService.viewAllTreatments();
    }

        
private static void viewTreatmentsByRecord() throws SQLException {

    System.out.println("\n===== VIEW PATIENT TREATMENTS =====");

    // Step 1: Search for patient
    String patientName = readLine("Enter patient name: ");

    if (patientName == null || patientName.isBlank()) {
        System.out.println("Patient name cannot be empty.");
        return;
    }

    List<Patient> patients =
            hospitalService.searchPatientsByName(patientName);

    if (patients.isEmpty()) {
        System.out.println("No matching patients found.");
        return;
    }

    // Step 2: Select patient
    System.out.println("\nMatching Patients:");

    for (int i = 0; i < patients.size(); i++) {
        Patient patient = patients.get(i);

        System.out.println(
                (i + 1) + ". " + patient.getName()
                + " | Age: " + patient.getAge()
                + " | Phone: " + patient.getPhone());
    }

    System.out.print("Select patient number: ");
    int patientChoice = readInt();

    if (patientChoice < 1 || patientChoice > patients.size()) {
        System.out.println("Invalid patient selection.");
        return;
    }

    Patient selectedPatient = patients.get(patientChoice - 1);

    // Step 3: Get patient's medical records
    List<MedicalRecord> records =
            hospitalService.getMedicalRecordsByPatient(
                    selectedPatient.getId());

    if (records.isEmpty()) {
        System.out.println(
                "No medical records found for this patient.");
        return;
    }

    // Step 4: Select medical record
    System.out.println(
            "\nMedical Records for " + selectedPatient.getName() + ":");

    DateTimeFormatter dateFormat =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    for (int i = 0; i < records.size(); i++) {
        MedicalRecord record = records.get(i);

        System.out.println(
                (i + 1) + ". Diagnosis: " + record.getDiagnosis()
                + " | Date: "
                + record.getRecordDate().format(dateFormat));
    }

    System.out.print("Select medical record number: ");
    int recordChoice = readInt();

    if (recordChoice < 1 || recordChoice > records.size()) {
        System.out.println("Invalid medical record selection.");
        return;
    }

    MedicalRecord selectedRecord = records.get(recordChoice - 1);

    // Step 5: Display treatments for the selected record
    System.out.println("\n===== TREATMENTS =====");
    System.out.println("Patient: " + selectedPatient.getName());
    System.out.println("Diagnosis: " + selectedRecord.getDiagnosis());
    System.out.println(
            "Record Date: "
            + selectedRecord.getRecordDate().format(dateFormat));

    hospitalService.viewTreatmentsByRecord(
            selectedRecord.getRecordId());
}


private static void addMedicine() throws SQLException {
    System.out.println("\n===== ADD MEDICINE =====");

    String name = readLine("Enter medicine name: ");

    if (name == null || name.isBlank()) {
        System.out.println("Medicine name cannot be empty.");
        return;
    }

    java.math.BigDecimal price;

    try {
        price = new java.math.BigDecimal(
                readLine("Enter unit price (Rs.): ").trim()
        );

        if (price.signum() < 0 || price.scale() > 2) {
            System.out.println(
                    "Price must be non-negative with at most 2 decimal places."
            );
            return;
        }
    } catch (NumberFormatException e) {
        System.out.println("Invalid price.");
        return;
    }

    System.out.print("Enter stock quantity: ");
    int quantity = readInt();

    if (quantity < 0) {
        System.out.println("Stock quantity cannot be negative.");
        return;
    }

    Medicine medicine = new Medicine(
            name.trim(), price, quantity
    );

    hospitalService.addMedicine(medicine);
}

private static void searchMedicine() throws SQLException {
    System.out.println("\n===== SEARCH MEDICINE =====");

    String name = readLine("Enter medicine name to search: ");

    if (name == null || name.isBlank()) {
        System.out.println("Please enter a medicine name.");
        return;
    }

    List<Medicine> medicines =
            hospitalService.searchMedicinesByName(name);

    if (medicines.isEmpty()) {
        System.out.println("No matching medicines found.");
        return;
    }

    for (Medicine medicine : medicines) {
        medicine.displayDetails();
    }
}

private static void updateMedicine() throws SQLException {
    System.out.println("\n===== UPDATE MEDICINE =====");

    String name = readLine("Enter medicine name to search: ");

    if (name == null || name.isBlank()) {
        System.out.println("Please enter a medicine name.");
        return;
    }

    List<Medicine> medicines =
            hospitalService.searchMedicinesByName(name);

    if (medicines.isEmpty()) {
        System.out.println("No matching medicines found.");
        return;
    }

    System.out.println("\nMatching Medicines:");

    for (int i = 0; i < medicines.size(); i++) {
        Medicine medicine = medicines.get(i);

        System.out.println(
                (i + 1) + ". ID: " + medicine.getMedicineId()
                + " | " + medicine.getName()
                + " | Price: Rs. " + medicine.getUnitPrice()
                + " | Stock: " + medicine.getStockQuantity()
        );
    }

    System.out.print("Select medicine number to update: ");
    int choice = readInt();

    if (choice < 1 || choice > medicines.size()) {
        System.out.println("Invalid selection.");
        return;
    }

    Medicine selected = medicines.get(choice - 1);

    System.out.println("\nSelected Medicine:");
    selected.displayDetails();

    String newName = readLine(
            "Enter new name (press Enter to keep current): "
    );

    if (newName != null && !newName.isBlank()) {
        selected.setName(newName.trim());
    }

    String priceInput = readLine(
            "Enter new unit price (press Enter to keep current): "
    );

    if (priceInput != null && !priceInput.isBlank()) {
        try {
            java.math.BigDecimal newPrice =
                    new java.math.BigDecimal(priceInput.trim());

            if (newPrice.signum() < 0 || newPrice.scale() > 2) {
                System.out.println("Invalid price.");
                return;
            }

            selected.setUnitPrice(newPrice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price.");
            return;
        }
    }

    String stockInput = readLine(
            "Enter new stock quantity (press Enter to keep current): "
    );

    if (stockInput != null && !stockInput.isBlank()) {
        try {
            int newStock = Integer.parseInt(stockInput.trim());

            if (newStock < 0) {
                System.out.println(
                        "Stock quantity cannot be negative."
                );
                return;
            }

            selected.setStockQuantity(newStock);
        } catch (NumberFormatException e) {
            System.out.println("Invalid stock quantity.");
            return;
        }
    }

    System.out.print("Save these changes? (Y/N): ");
    String confirmation = readLine("").trim();

    if (confirmation.equalsIgnoreCase("Y")) {
        boolean updated = hospitalService.updateMedicine(selected);

        System.out.println(updated
                ? "Medicine updated successfully."
                : "Medicine was not updated.");
    } else {
        System.out.println("Update cancelled.");
    }
}

private static void viewLowStockMedicines() throws SQLException {
    System.out.println("\n===== LOW-STOCK MEDICINES =====");

    System.out.print("Enter stock threshold: ");
    int threshold = readInt();

    if (threshold < 0) {
        System.out.println("Threshold cannot be negative.");
        return;
    }

    hospitalService.viewLowStockMedicines(threshold);
}

    private static void createPrescription() throws SQLException {
        System.out.println("\n===== CREATE PRESCRIPTION =====");

        // 1. Select patient.
        Patient patient = selectPatientByName();
        if (patient == null) {
            return;
        }

        // 2. Retrieve and select one of the patient's medical records.
        List<MedicalRecord> records =
                hospitalService.getMedicalRecordsByPatient(patient.getId());

        if (records.isEmpty()) {
            System.out.println(
                    "This patient has no medical records. Add one first.");
            return;
        }

        MedicalRecord record = selectMedicalRecord(records, patient.getName());
        if (record == null) {
            return;
        }

        // 3. Validate the prescription date.
        String dateInput = readLine("Enter prescription date (DDMMYYYY): ");
        if (dateInput == null) {
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateInput, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use DDMMYYYY, for example 27102026.");
            return;
        }

        if (date.isAfter(LocalDate.now())) {
            System.out.println("Prescription date cannot be in the future.");
            return;
        }

        // 4. Enter prescription instructions.
        String instructions = readLine("Enter prescription instructions: ");
        if (instructions == null || instructions.isBlank()) {
            System.out.println("Instructions cannot be empty.");
            return;
        }

        // 5. Collect one or more medicines before saving anything.
        List<PrescriptionItem> items = new java.util.ArrayList<>();
        java.util.Map<Integer, Integer> selectedQuantities =
                new java.util.HashMap<>();
        java.util.Map<Integer, String> selectedMedicineNames =
                new java.util.LinkedHashMap<>();

        while (true) {
            System.out.println("\n===== ADD MEDICINE TO PRESCRIPTION =====");
            String medicineName = readLine("Enter medicine name to search: ");

            if (medicineName == null || medicineName.isBlank()) {
                System.out.println("Medicine name cannot be empty.");
                if (items.isEmpty()) {
                    return;
                }
                break;
            }

            List<Medicine> medicines =
                    hospitalService.searchMedicinesByName(medicineName);

            if (medicines.isEmpty()) {
                System.out.println("No matching medicines found.");
                String retry = readLine("Search for another medicine? (Y/N): ");
                if (retry != null && retry.equalsIgnoreCase("Y")) {
                    continue;
                }
                if (items.isEmpty()) {
                    System.out.println("No medicines added. Prescription cancelled.");
                    return;
                }
                break;
            }

            System.out.println("\nMatching Medicines:");
            for (int i = 0; i < medicines.size(); i++) {
                Medicine medicine = medicines.get(i);
                int alreadySelected = selectedQuantities.getOrDefault(
                        medicine.getMedicineId(), 0);
                int remaining = medicine.getStockQuantity() - alreadySelected;

                System.out.println((i + 1) + ". " + medicine.getName()
                        + " | Price: Rs. " + medicine.getUnitPrice()
                        + " | Available stock: " + Math.max(remaining, 0));
            }

            System.out.print("Select medicine number: ");
            int medicineChoice = readInt();
            if (medicineChoice < 1 || medicineChoice > medicines.size()) {
                System.out.println("Invalid medicine selection. Try again.");
                continue;
            }

            Medicine medicine = medicines.get(medicineChoice - 1);
            int alreadySelected = selectedQuantities.getOrDefault(
                    medicine.getMedicineId(), 0);
            int remainingStock = medicine.getStockQuantity() - alreadySelected;

            if (remainingStock <= 0) {
                System.out.println("No stock remains for " + medicine.getName() + ".");
                continue;
            }

            String dosage = readLine(
                    "Enter dosage (e.g. 1 tablet twice daily): ");
            if (dosage == null || dosage.isBlank()) {
                System.out.println("Dosage cannot be empty.");
                continue;
            }

            String duration = readLine("Enter duration (e.g. 5 days): ");
            if (duration == null || duration.isBlank()) {
                System.out.println("Duration cannot be empty.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int quantity = readInt();
            if (quantity <= 0 || quantity > remainingStock) {
                System.out.println("Quantity must be between 1 and "
                        + remainingStock + ".");
                continue;
            }

            // The 5-argument constructor stores the medicine details in the draft.
            items.add(new PrescriptionItem(
                    0, medicine.getMedicineId(), dosage.trim(),
                    duration.trim(), quantity));

            selectedQuantities.put(
                    medicine.getMedicineId(), alreadySelected + quantity);
            selectedMedicineNames.put(
                    medicine.getMedicineId(), medicine.getName());

            System.out.println(medicine.getName() + " added to the prescription draft.");

            String another = readLine("Add another medicine? (Y/N): ");
            if (another == null || !another.equalsIgnoreCase("Y")) {
                break;
            }
        }

        if (items.isEmpty()) {
            System.out.println("No medicines were added. Prescription cancelled.");
            return;
        }

        // 6. Display one summary and ask for confirmation once.
        System.out.println("\n========== PRESCRIPTION SUMMARY ==========");
        System.out.println("Patient: " + patient.getName());
        System.out.println("Diagnosis: " + record.getDiagnosis());
        System.out.println("Prescription date: " + date.format(DISPLAY_DATE_FORMAT));
        System.out.println("Instructions: " + instructions);
        System.out.println("\nMedicines:");

        for (int i = 0; i < items.size(); i++) {
            PrescriptionItem item = items.get(i);
            String name = selectedMedicineNames.getOrDefault(
                    item.getMedicineId(), "Medicine ID " + item.getMedicineId());

            System.out.println((i + 1) + ". " + name
                    + " | Dosage: " + item.getDosage()
                    + " | Duration: " + item.getDuration()
                    + " | Quantity: " + item.getQuantity());
        }

        System.out.println("==========================================");
        String confirmation = readLine(
                "Save prescription and dispense these medicines? (Y/N): ");

        if (confirmation == null || !confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Prescription creation cancelled. No changes were saved.");
            return;
        }

        // 7. Save the prescription, its items, and stock changes transactionally.
        Prescription prescription = new Prescription(
                record.getRecordId(), date, instructions.trim());

        int prescriptionId = hospitalService.createPrescriptionWithItems(
                prescription, items);

        if (prescriptionId > 0) {
            System.out.println("Prescription created successfully for "
                    + patient.getName() + ".");
            System.out.println("Prescription ID: " + prescriptionId);
        } else {
            System.out.println(
                    "Prescription was not saved. Check the error above.");
        }
    }

    private static void addMedicineToPrescription() throws SQLException {
        System.out.println("\n===== ADD MEDICINE TO PRESCRIPTION =====");
        Patient patient = selectPatientByName();
        if (patient == null) return;

        List<MedicalRecord> records = hospitalService.getMedicalRecordsByPatient(patient.getId());
        if (records.isEmpty()) {
            System.out.println("This patient has no medical records.");
            return;
        }
        MedicalRecord record = selectMedicalRecord(records, patient.getName());
        if (record == null) return;

        List<Integer> prescriptionIds = hospitalService.getPrescriptionIdsByRecord(record.getRecordId());
        if (prescriptionIds.isEmpty()) {
            System.out.println("No prescriptions found for this medical record. Create one first.");
            return;
        }

        hospitalService.viewPrescriptionsByRecord(record.getRecordId());
        System.out.println("\nSelect a prescription:");
        for (int i = 0; i < prescriptionIds.size(); i++) {
            System.out.println((i + 1) + ". Prescription option " + (i + 1));
        }
        System.out.print("Select prescription number: ");
        int prescriptionChoice = readInt();
        if (prescriptionChoice < 1 || prescriptionChoice > prescriptionIds.size()) {
            System.out.println("Invalid prescription selection.");
            return;
        }
        int prescriptionId = prescriptionIds.get(prescriptionChoice - 1);

        String medicineName = readLine("Enter medicine name to search: ");
        if (medicineName == null || medicineName.isBlank()) {
            System.out.println("Medicine name cannot be empty.");
            return;
        }
        List<Medicine> medicines = hospitalService.searchMedicinesByName(medicineName);
        if (medicines.isEmpty()) {
            System.out.println("No matching medicines found.");
            return;
        }
        System.out.println("\nMatching Medicines:");
        for (int i = 0; i < medicines.size(); i++) {
            Medicine m = medicines.get(i);
            System.out.println((i + 1) + ". " + m.getName()
                    + " | Price: Rs. " + m.getUnitPrice()
                    + " | Stock: " + m.getStockQuantity());
        }
        System.out.print("Select medicine number: ");
        int medicineChoice = readInt();
        if (medicineChoice < 1 || medicineChoice > medicines.size()) {
            System.out.println("Invalid medicine selection.");
            return;
        }
        Medicine medicine = medicines.get(medicineChoice - 1);
        if (medicine.getStockQuantity() < 1) {
            System.out.println("This medicine is out of stock.");
            return;
        }

        String dosage = readLine("Enter dosage (e.g. 1 tablet twice daily): ");
        if (dosage == null || dosage.isBlank()) {
            System.out.println("Dosage cannot be empty.");
            return;
        }
        String duration = readLine("Enter duration (e.g. 5 days): ");
        if (duration == null || duration.isBlank()) {
            System.out.println("Duration cannot be empty.");
            return;
        }
        System.out.print("Enter quantity: ");
        int quantity = readInt();
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        String confirm = readLine("Add " + medicine.getName()
                + " to " + patient.getName() + "'s prescription? (Y/N): ");
        if (confirm == null || !confirm.equalsIgnoreCase("Y")) {
            System.out.println("Operation cancelled.");
            return;
        }

       boolean success = hospitalService.addPrescriptionItem(
                prescriptionId,
                medicine.getMedicineId(),
                dosage,
                duration,
                quantity
            );

            if (!success) {
                System.out.println(
                        "Medicine was not added or dispensed. "
                        + "Check the stock and try again."
                );
            }
        }

    private static void viewPrescriptionsByRecord() throws SQLException {
        System.out.println("\n===== VIEW PRESCRIPTIONS =====");

        Patient patient = selectPatientByName();
        if (patient == null) {
            return;
        }

        List<MedicalRecord> records =
                hospitalService.getMedicalRecordsByPatient(patient.getId());

        if (records.isEmpty()) {
            System.out.println("No medical records found for this patient.");
            return;
        }

        MedicalRecord record = selectMedicalRecord(records, patient.getName());
        if (record == null) {
            return;
        }

        List<Integer> prescriptionIds =
                hospitalService.getPrescriptionIdsByRecord(record.getRecordId());

        if (prescriptionIds.isEmpty()) {
            System.out.println("No prescriptions found for this medical record.");
            return;
        }

        System.out.println("\n===== SAVED PRESCRIPTIONS =====");
        hospitalService.viewPrescriptionsByRecord(record.getRecordId());

        String exportChoice = readLine(
                "\nGenerate a prescription text file for this medical record? (Y/N): ");

        if (exportChoice == null || !exportChoice.equalsIgnoreCase("Y")) {
            return;
        }

        try {
            java.nio.file.Path filePath =
                    PrescriptionFileExporter.exportPrescriptionsForRecord(
                            record.getRecordId());

            System.out.println("Prescription file generated successfully:");
            System.out.println(filePath.toAbsolutePath());
        } catch (java.io.IOException e) {
            System.out.println("Could not generate prescription file: "
                    + e.getMessage());
        }
    }

    private static Patient selectPatientByName() throws SQLException {
        String name = readLine("Enter patient name to search: ");
        if (name == null || name.isBlank()) {
            System.out.println("Patient name cannot be empty.");
            return null;
        }

        List<Patient> patients = hospitalService.searchPatientsByName(name);
        if (patients.isEmpty()) {
            System.out.println("No matching patients found.");
            return null;
        }

        System.out.println("\nMatching Patients:");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            System.out.println((i + 1) + ". " + p.getName()
                    + " | Age: " + p.getAge()
                    + " | Phone: " + p.getPhone());
        }
        System.out.print("Select patient number: ");
        int choice = readInt();
        if (choice < 1 || choice > patients.size()) {
            System.out.println("Invalid patient selection.");
            return null;
        }
        return patients.get(choice - 1);
    }

    private static MedicalRecord selectMedicalRecord(
            List<MedicalRecord> records, String patientName) {
        System.out.println("\nMedical records for " + patientName + ":");
        for (int i = 0; i < records.size(); i++) {
            MedicalRecord record = records.get(i);
            System.out.println((i + 1) + ". Diagnosis: " + record.getDiagnosis()
                    + " | Date: " + record.getRecordDate().format(DISPLAY_DATE_FORMAT));
        }
        System.out.print("Select medical record number: ");
        int choice = readInt();
        if (choice < 1 || choice > records.size()) {
            System.out.println("Invalid medical record selection.");
            return null;
        }
        return records.get(choice - 1);
    }


    
    // ==================== BILLING MENU METHODS ====================

    // private static Patient selectPatientByName() throws SQLException {
    //     String name = readLine("Enter patient name to search: ");

    //     if (name == null || name.isBlank()) {
    //         System.out.println("Patient name cannot be empty.");
    //         return null;
    //     }

    //     List<Patient> patients =
    //             hospitalService.searchPatientsByName(name);

    //     if (patients.isEmpty()) {
    //         System.out.println("No matching patients found.");
    //         return null;
    //     }

    //     System.out.println("\nMatching Patients:");

    //     for (int i = 0; i < patients.size(); i++) {
    //         Patient p = patients.get(i);

    //         System.out.println(
    //                 (i + 1) + ". " + p.getName()
    //                 + " | Age: " + p.getAge()
    //                 + " | Phone: " + p.getPhone());
    //     }

    //     System.out.print("Select patient number: ");
    //     int choice = readInt();

    //     if (choice < 1 || choice > patients.size()) {
    //         System.out.println("Invalid patient selection.");
    //         return null;
    //     }

    //     return patients.get(choice - 1);
    // }

    private static void generatePatientBill() throws SQLException {
        System.out.println("\n===== GENERATE PATIENT BILL =====");

        Patient patient = selectPatientByName();

        if (patient == null) {
            return;
        }

        List<BillItem> items = new java.util.ArrayList<>();

        while (true) {
            String description = readLine("Enter charge description: ");

            if (description == null || description.isBlank()) {
                System.out.println("Description cannot be empty.");
                return;
            }

            String amountInput =
                    readLine("Enter charge amount (Rs.): ");

            if (amountInput == null) {
                return;
            }

            BigDecimal amount;

            try {
                amount = new BigDecimal(amountInput);

                if (amount.signum() < 0 || amount.scale() > 2) {
                    System.out.println(
                            "Amount must be non-negative with up to "
                            + "two decimal places.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount.");
                return;
            }

            items.add(new BillItem(description.trim(), amount));

            System.out.println("Charge added.");

            String another = readLine("Add another charge? (Y/N): ");

            if (another == null
                    || !another.equalsIgnoreCase("Y")) {
                break;
            }
        }

        if (items.isEmpty()) {
            System.out.println("No charges entered.");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;

        System.out.println("\n===== BILL SUMMARY =====");
        System.out.println("Patient: " + patient.getName());

        for (BillItem item : items) {
            item.displayDetails();
            total = total.add(item.getAmount());
        }

        System.out.println("Total: Rs. " + total);

        String confirm = readLine("Generate this bill? (Y/N): ");

        if (confirm == null || !confirm.equalsIgnoreCase("Y")) {
            System.out.println("Bill generation cancelled.");
            return;
        }

        int billId = hospitalService.createBill(
                patient.getId(), LocalDate.now(), items);

        if (billId > 0) {
            System.out.println("Bill created successfully.");
            System.out.println("Bill reference: " + billId);
        } else {
            System.out.println("Bill could not be created.");
        }
    }

    private static void viewPatientBillingHistory()
            throws SQLException {

        System.out.println("\n===== PATIENT BILLING HISTORY =====");

        Patient patient = selectPatientByName();

        if (patient == null) {
            return;
        }

        System.out.println("Patient: " + patient.getName());

        hospitalService.viewBillingHistory(patient.getId());
    }

    private static void viewSelectedBillDetails()
            throws SQLException {

        System.out.println("\n===== VIEW BILL DETAILS =====");

        Patient patient = selectPatientByName();

        if (patient == null) {
            return;
        }

        hospitalService.viewBillingHistory(patient.getId());

        System.out.print(
                "\nEnter the Bill ID shown above to view its details: ");

        int billId = readInt();

        if (billId <= 0) {
            System.out.println("Invalid bill reference.");
            return;
        }

        hospitalService.viewBillDetails(billId);
    }

    private static void recordPatientPayment()
            throws SQLException {

        System.out.println("\n===== RECORD PATIENT PAYMENT =====");

        Patient patient = selectPatientByName();

        if (patient == null) {
            return;
        }

        hospitalService.viewBillingHistory(patient.getId());

        System.out.print("\nEnter the Bill ID shown above: ");
        int billId = readInt();

        if (billId <= 0) {
            System.out.println("Invalid bill reference.");
            return;
        }

        String amountInput =
                readLine("Enter payment amount (Rs.): ");

        if (amountInput == null) {
            return;
        }

        BigDecimal amount;

        try {
            amount = new BigDecimal(amountInput);

            if (amount.signum() <= 0 || amount.scale() > 2) {
                System.out.println(
                        "Payment must be positive with up to "
                        + "two decimal places.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid payment amount.");
            return;
        }

        System.out.println("\nPayment Methods");
        System.out.println("1. Cash");
        System.out.println("2. UPI");
        System.out.println("3. Card");
        System.out.println("4. Bank Transfer");

        System.out.print("Select payment method: ");
        int methodChoice = readInt();

        if (methodChoice < 1 || methodChoice > 4) {
            System.out.println("Invalid payment method.");
            return;
        }

        String confirm = readLine(
                "Confirm payment of Rs. " + amount + "? (Y/N): ");

        if (confirm == null || !confirm.equalsIgnoreCase("Y")) {
            System.out.println("Payment cancelled.");
            return;
        }

        // Runtime polymorphism: the selected subclass determines the payment method.
        Payment payment = switch (methodChoice) {
            case 1 -> new CashPayment(billId, amount);
            case 2 -> new UpiPayment(billId, amount);
            case 3 -> new CardPayment(billId, amount);
            case 4 -> new BankTransferPayment(billId, amount);
            default -> throw new IllegalStateException("Unexpected payment choice");
        };

        payment.process(hospitalService);
    }


}

