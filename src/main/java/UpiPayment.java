import java.math.BigDecimal;

public class UpiPayment extends Payment {
    public UpiPayment(int billId, BigDecimal amount) {
        super(billId, amount);
    }

    @Override
    public String getPaymentMethod() {
        return "UPI";
    }
}
