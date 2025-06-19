package dto;

public class UpdateTransactionRequest {
    private long userId;
    private long bookId;
    private String returnDate;
    private String status;

    public UpdateTransactionRequest(long userId, long bookId, String returnDate, String status) {
        this.userId = userId;
        this.bookId = bookId;
        this.returnDate = returnDate;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getBookId() {
        return bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
    public String getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
