package dto;

public class UpdateBookRequest {
    public String author;
    public String title;
    public String isbn;
    public long quantity;

    public UpdateBookRequest(String author, String title, String isbn, long quantity) {
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.quantity = quantity;
    }
}
