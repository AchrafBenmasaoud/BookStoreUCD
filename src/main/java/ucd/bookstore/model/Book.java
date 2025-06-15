package ucd.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    @NotBlank
    private String year;
    @NotBlank
    private double price;
    @NotNull
    private int copies;

    public Book(String title, List<Author> authors, String year, double price, int copies) {
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.price = price;
        this.copies = copies;
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
}

// title, author, year, price, and number of copies.
