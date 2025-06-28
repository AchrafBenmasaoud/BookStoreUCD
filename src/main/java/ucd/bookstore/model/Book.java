package ucd.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String title;

    @ManyToMany
    @JoinTable(
            name = "book_by_author",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors = new ArrayList<>();

    @NotBlank
    private String year;
    @NotNull
    private double price;
    @NotNull
    private int copies;
    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(regexp = "\\d{10,13}", message = "ISBN must contain only digits (10 to 13 digits)")
    private String isbn;

    public Book(String isbn, int copies, double price, String year, List<Author> authors, String title) {
        this.isbn = isbn;
        this.copies = copies;
        this.price = price;
        this.year = year;
        this.authors = authors;
        this.title = title;
    }

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}

// title, author, year, price, and number of copies.
