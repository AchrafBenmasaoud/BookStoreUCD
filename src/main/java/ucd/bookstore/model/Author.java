package ucd.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "author")

public class Author {
    @Id
    @GeneratedValue
    private Long author_id;

    @NotBlank
    private String authorName;

    @NotBlank
    private String authorSurname;

    @ManyToMany
    private List<Book> books;

    public Author(String authorName, String authorSurname, List<Book> books) {
        this.authorName = authorName;
        this.authorSurname = authorSurname;
        this.books = books;
    }

    public Author() {
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

}

