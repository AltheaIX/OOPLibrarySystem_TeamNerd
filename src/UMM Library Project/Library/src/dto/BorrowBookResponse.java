package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import model.BorrowedBook;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BorrowBookResponse {
    public int status;
    public String message;
    public BorrowedBook data;
}
