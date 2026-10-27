
import java.math.BigDecimal;

public class BillItem {

    private int billItemId;
    private int billId;
    private String description;
    private BigDecimal amount;

    public BillItem(String description, BigDecimal amount) {
        this.description = description;
        this.amount = amount;
    }

    public BillItem(int billItemId, int billId,
                    String description, BigDecimal amount) {
        this.billItemId = billItemId;
        this.billId = billId;
        this.description = description;
        this.amount = amount;
    }

    public int getBillItemId() {
        return billItemId;
    }

    public int getBillId() {
        return billId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setBillItemId(int billItemId) {
        this.billItemId = billItemId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void displayDetails() {
        System.out.println("Description: " + description);
        System.out.println("Amount: Rs. " + amount);
    }
}
