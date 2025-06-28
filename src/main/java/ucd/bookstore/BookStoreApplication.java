package ucd.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ucd.bookstore.model.*;
import ucd.bookstore.repository.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

@SpringBootApplication
public class BookStoreApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        User user = new User("Admin", "Admin", "admin@hotmail.com", "admin1234", "0618389301","Thinkpinklaan 77", LocalDate.of(2000,10, 24), UserRole.ADMIN);

        // Hash the password using SHA-256
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
            String hashedPassword = HexFormat.of().formatHex(hash);
            user.setPassword(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // Ideally log this properly or handle as needed
            throw new RuntimeException("Error hashing password", e);
        }

        // Create and assign a cart
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        userRepository.save(user);

        // 2. Create empty book list placeholders for authors (will populate later)
        Author rowling = new Author("J.K.", "Rowling");
        Author tolkien = new Author("J.R.R.", "Tolkien");
        Author martin = new Author("George R.R.", "Martin");
        Author orwell = new Author("George", "Orwell");
        Author austen = new Author("Jane", "Austen");

        List<Author> authors = authorRepository.saveAll(List.of(rowling, tolkien, martin, orwell, austen));

        // 3. Create Books
        List<Book> books = List.of(
                new Book("9780439708180", 7, 29.99, "1997", List.of(rowling), "Harry Potter and the Sorcerer's Stone"),
                new Book("9780261102217", 5, 24.50, "1937", List.of(tolkien), "The Hobbit"),
                new Book("9780553573404", 8, 34.99, "1996", List.of(martin), "A Game of Thrones"),
                new Book("9780451524935", 3, 19.99, "1949", List.of(orwell), "1984"),
                new Book("9781503290563", 10, 14.95, "1813", List.of(austen), "Pride and Prejudice"),
                new Book("9780439064873", 6, 27.99, "1998", List.of(rowling), "Harry Potter and the Chamber of Secrets"),
                new Book("9780261102385", 4, 39.99, "1954", List.of(tolkien), "The Lord of the Rings"),
                new Book("9780451526342", 9, 15.99, "1945", List.of(orwell), "Animal Farm"),
                new Book("9781503212732", 2, 13.99, "1815", List.of(austen), "Emma"),
                new Book("9780553579901", 5, 36.99, "1998", List.of(martin), "A Clash of Kings")
        );

        // Save books
        bookRepository.saveAll(books);

        // 4. Add books to authors' lists and update authors
        for (Book book : books) {
            for (Author author : book.getAuthors()) {
                author.getBooks().add(book);
            }
        }

        authorRepository.saveAll(List.of(rowling, tolkien, martin, orwell, austen));

        System.out.println("✅ Admin and sample books with authors loaded.");
    }


}
