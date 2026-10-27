import java.math.BigDecimal;

public class BankTransferPayment extends Payment {
    public BankTransferPayment(int billId, BigDecimal amount) {
        super(billId, amount);
    }

    @Override
    public String getPaymentMethod() {
        return "BANK_TRANSFER";
    }
}
