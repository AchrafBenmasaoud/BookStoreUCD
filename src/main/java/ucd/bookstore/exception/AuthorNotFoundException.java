package ucd.bookstore.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(long author_id) {
        super(String.format("Author is not found with id : '%s'", author_id));
  }
}
