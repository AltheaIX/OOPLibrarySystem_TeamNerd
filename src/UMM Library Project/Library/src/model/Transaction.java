package model;

import java.time.LocalDate;

public class Transaction {
    private String id;
    private String userId;
    private String bookId;
    private String createdAt;
    private String returnDate;
    private String status;

    public Transaction() {

    }

    public Transaction(String id, String userId, String bookId, String createdAt, String returnDate, String status) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.createdAt = createdAt;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}