import java.math.BigDecimal;
import java.sql.SQLException;

/** Abstract payment type. Concrete payment methods supply their own method name. */
public abstract class Payment {
    private final int billId;
    private final BigDecimal amount;

    protected Payment(int billId, BigDecimal amount) {
        this.billId = billId;
        this.amount = amount;
    }

    public int getBillId() { return billId; }
    public BigDecimal getAmount() { return amount; }

    public abstract String getPaymentMethod();

    public boolean process(HospitalService hospitalService) throws SQLException {
        return hospitalService.recordPayment(billId, amount, getPaymentMethod());
    }
}
