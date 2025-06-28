package ucd.bookstore.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucd.bookstore.exception.AuthorNotFoundException;
import ucd.bookstore.model.Author;
import ucd.bookstore.model.Book;
import ucd.bookstore.repository.AuthorRepository;
import ucd.bookstore.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/author") // default endpoint for controller
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    // Get All Author
    @GetMapping
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        return "author"; // views/author/author.html
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("author", new Author());
        return "add-author"; // templates/author/add-author.html
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id)));
        return "update-author"; // templates/author/update-book.html
    }

    // Create a new Author
    @PostMapping()
    public String newAuthor(@Validated @ModelAttribute("author") Author author)
    {
      authorRepository.save(author);
      return "redirect:/author";
    }

    // Delete author

    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        // In your service or controller before authorRepository.delete(author);
        List<Book> books = new ArrayList<>(author.getBooks());

        for (Book book : books) {
            if (book.getAuthors().size() == 1) {
                // This author is the only one -> delete the book
                bookRepository.delete(book);
            } else {
                // Remove the author from this book
                book.getAuthors().remove(author);
                bookRepository.save(book);
            }
        }

        // Now safe to delete the author
        authorRepository.delete(author);
        authorRepository.deleteById(id);
        return "redirect:/author";
    }
    // Update Author
    @PostMapping("/update")
    public String updateAuthor(@Validated @ModelAttribute("author") Author author) {
        authorRepository.save(author);
        return "redirect:/author";
    }

}
