import java.math.BigDecimal;

public class CardPayment extends Payment {
    public CardPayment(int billId, BigDecimal amount) {
        super(billId, amount);
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }
}
