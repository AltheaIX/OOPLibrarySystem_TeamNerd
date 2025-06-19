package GUI;

import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class LoanRecord {
    private final SimpleStringProperty title;
    private final SimpleObjectProperty<LocalDate> borrowDate;
    private final SimpleObjectProperty<LocalDate> returnDate;

    public LoanRecord(String title, LocalDate borrowDate, LocalDate returnDate) {
        this.title = new SimpleStringProperty(title);
        this.borrowDate = new SimpleObjectProperty<>(borrowDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleObjectProperty<LocalDate> borrowDateProperty() {
        return borrowDate;
    }

    public SimpleObjectProperty<LocalDate> returnDateProperty() {
        return returnDate;
    }
}
