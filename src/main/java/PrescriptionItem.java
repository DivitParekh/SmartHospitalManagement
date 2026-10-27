
public class PrescriptionItem {

    private int itemId;
    private int prescriptionId;
    private int medicineId;
    private String dosage;
    private String duration;
    private int quantity;

    // Constructor for a new prescription item
    public PrescriptionItem(int prescriptionId, int medicineId,
                            String dosage, String duration,
                            int quantity) {
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.duration = duration;
        this.quantity = quantity;
    }

    // Constructor for an existing prescription item
    public PrescriptionItem(int itemId, int prescriptionId,
                            int medicineId, String dosage,
                            String duration, int quantity) {
        this.itemId = itemId;
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.duration = duration;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDuration() {
        return duration;
    }

    public int getQuantity() {
        return quantity;
    }

    public void displayDetails() {
        System.out.println("------------------------------");
        System.out.println("Item ID: " + itemId);
        System.out.println("Prescription ID: " + prescriptionId);
        System.out.println("Medicine ID: " + medicineId);
        System.out.println("Dosage: " + dosage);
        System.out.println("Duration: " + duration);
        System.out.println("Quantity: " + quantity);
        System.out.println("------------------------------");
    }
}
