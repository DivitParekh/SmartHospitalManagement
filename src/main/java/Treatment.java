
import java.math.BigDecimal;

public class Treatment {

    private int treatmentId;
    private int recordId;
    private String description;
    private BigDecimal cost;

    // Constructor for creating a new treatment
    public Treatment(int recordId, String description,
                     BigDecimal cost) {
        this.recordId = recordId;
        this.description = description;
        this.cost = cost;
    }

    // Constructor for a treatment retrieved from the database
    public Treatment(int treatmentId, int recordId,
                     String description, BigDecimal cost) {
        this.treatmentId = treatmentId;
        this.recordId = recordId;
        this.description = description;
        this.cost = cost;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void displayDetails() {
        System.out.println("Treatment ID: " + treatmentId);
        System.out.println("Medical Record ID: " + recordId);
        System.out.println("Description: " + description);
        System.out.println("Cost: Rs. " + cost);
    }
}
