package dto;

public class ReturnBookRequest {
    private long transactionId;

    public ReturnBookRequest(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }
}
