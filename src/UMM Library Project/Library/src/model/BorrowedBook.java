package model;

import java.time.LocalDate;

public class BorrowedBook {
    private long id;
    private long userId;
    private long bookId;
    private String returnDate;
    private String status;
    private String createdAt;

    public BorrowedBook() {

    }

    public BorrowedBook(long id, long userId, long bookId, String returnDate, String status, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.returnDate = returnDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
