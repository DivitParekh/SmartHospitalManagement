
import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill {

    private int billId;
    private int patientId;
    private LocalDate billDate;
    private BigDecimal totalAmount;
    private String status;

    public Bill(int patientId, LocalDate billDate,
                BigDecimal totalAmount, String status) {
        this.patientId = patientId;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Bill(int billId, int patientId, LocalDate billDate,
                BigDecimal totalAmount, String status) {
        this.billId = billId;
        this.patientId = patientId;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getBillId() {
        return billId;
    }

    public int getPatientId() {
        return patientId;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayDetails() {
        System.out.println("Bill ID: " + billId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Bill Date: " + billDate);
        System.out.println("Total Amount: Rs. " + totalAmount);
        System.out.println("Status: " + status);
    }
}
