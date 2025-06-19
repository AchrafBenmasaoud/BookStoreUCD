package ucd.bookstore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucd.bookstore.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByOrderByCopiesDesc();
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByIsbnContainingIgnoreCase(String isbn);
    List<Book> findByAuthors_AuthorNameContainingIgnoreCase(String name);
    List<Book> findByAuthors_AuthorSurnameContainingIgnoreCase(String surname);
}
