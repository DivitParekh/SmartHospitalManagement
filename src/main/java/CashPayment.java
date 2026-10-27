import java.math.BigDecimal;

public class CashPayment extends Payment {
    public CashPayment(int billId, BigDecimal amount) {
        super(billId, amount);
    }

    @Override
    public String getPaymentMethod() {
        return "CASH";
    }
}
