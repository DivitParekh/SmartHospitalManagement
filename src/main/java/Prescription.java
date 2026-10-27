
import java.time.LocalDate;

public class Prescription {

    private int prescriptionId;
    private int recordId;
    private LocalDate prescriptionDate;
    private String instructions;

    // Constructor for a new prescription
    public Prescription(int recordId, LocalDate prescriptionDate,
                        String instructions) {
        this.recordId = recordId;
        this.prescriptionDate = prescriptionDate;
        this.instructions = instructions;
    }

    // Constructor for an existing prescription
    public Prescription(int prescriptionId, int recordId,
                        LocalDate prescriptionDate,
                        String instructions) {
        this.prescriptionId = prescriptionId;
        this.recordId = recordId;
        this.prescriptionDate = prescriptionDate;
        this.instructions = instructions;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public int getRecordId() {
        return recordId;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void displayDetails() {
        System.out.println("------------------------------");
        System.out.println("Prescription ID: " + prescriptionId);
        System.out.println("Medical Record ID: " + recordId);
        System.out.println("Prescription Date: " + prescriptionDate);
        System.out.println("Instructions: " + instructions);
        System.out.println("------------------------------");
    }
}
