package ucd.bookstore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucd.bookstore.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
