package ucd.bookstore.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(long book_id) {
        super(String.format("Book is not found with id : '%s'", book_id));
    }
}

