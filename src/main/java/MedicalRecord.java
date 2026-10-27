
import java.time.LocalDate;

public class MedicalRecord {

    private int recordId;
    private int patientId;
    private int doctorId;
    private Integer appointmentId;
    private String diagnosis;
    private LocalDate recordDate;

    // Constructor for creating a new medical record
    public MedicalRecord(int patientId, int doctorId,
                         Integer appointmentId,
                         String diagnosis, LocalDate recordDate) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.recordDate = recordDate;
    }

    // Constructor for a record retrieved from the database
    public MedicalRecord(int recordId, int patientId, int doctorId,
                         Integer appointmentId,
                         String diagnosis, LocalDate recordDate) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.recordDate = recordDate;
    }

    public int getRecordId() {
        return recordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void displayDetails() {
        System.out.println("Record ID: " + recordId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Appointment ID: "
                + (appointmentId == null
                        ? "Not linked" : appointmentId));
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("Record Date: " + recordDate);
    }
}
