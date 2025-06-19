package dto;

public class BorrowBookRequest {
    private long userId;
    private long bookId;
    private long days;

    public BorrowBookRequest(long userId, long bookId, long days) {
        this.userId = userId;
        this.bookId = bookId;
        this.days = days;
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
    public long getDays() {
        return days;
    }
    public void setDays(long days) {
        this.days = days;
    }
}
