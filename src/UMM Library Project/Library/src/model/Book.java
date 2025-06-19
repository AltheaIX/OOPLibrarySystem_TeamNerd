package model;

import java.time.LocalDate;

public class Book {
    private long id;
    private String title;
    private String description;
    private String genre;
    private boolean favourite;
    private boolean borrowed;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String coverUrl;

    public Book(String title, String description, String genre, boolean favourite, String coverUrl) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.favourite = favourite;
        this.coverUrl = coverUrl;
        this.borrowed = false;
        this.borrowDate = null;
        this.returnDate = null;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getGenre() { return genre; }
    public boolean isFavourite() { return favourite; }
    public void setFavourite(boolean favourite) { this.favourite = favourite; }
    public String getCoverUrl() { return coverUrl; }
    public boolean isBorrowed() { return borrowed; }
    public void setBorrowed(boolean borrowed) { this.borrowed = borrowed; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
}
