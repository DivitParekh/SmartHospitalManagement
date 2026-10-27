
import java.math.BigDecimal;

public class Medicine {

    private int medicineId;
    private String name;
    private BigDecimal unitPrice;
    private int stockQuantity;

    // Constructor for adding a new medicine
    public Medicine(String name, BigDecimal unitPrice,
                    int stockQuantity) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
    }

    // Constructor for retrieving an existing medicine
    public Medicine(int medicineId, String name,
                    BigDecimal unitPrice, int stockQuantity) {
        this.medicineId = medicineId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void displayDetails() {
        System.out.println("------------------------------");
        System.out.println("Medicine ID: " + medicineId);
        System.out.println("Medicine Name: " + name);
        System.out.println("Unit Price: Rs. " + unitPrice);
        System.out.println("Stock Quantity: " + stockQuantity);
        System.out.println("------------------------------");
    }
}
