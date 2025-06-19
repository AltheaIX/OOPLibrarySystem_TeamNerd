package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import model.BorrowedBook;

import java.util.List;

public class BorrowedBookResponse {
    public int status;
    public List<BorrowedBook> data;
}
