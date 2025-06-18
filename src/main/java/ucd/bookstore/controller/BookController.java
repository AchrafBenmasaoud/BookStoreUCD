package ucd.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucd.bookstore.exception.BookNotFoundException;
import ucd.bookstore.model.Book;
import ucd.bookstore.repository.AuthorRepository;
import ucd.bookstore.repository.BookRepository;

@Controller
@RequestMapping("/book") // default endpoint for controller
public class BookController {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("book", bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id)));
        return "update-book"; // templates/author/update-book.html
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("book", new Book());
        return "add-book"; // templates/author/add-book.html
    }

    // Create a new Book
    @PostMapping()
    public String newBook(@Validated @ModelAttribute("book") Book book) {
        bookRepository.save(book);
        return "redirect:/";
    }

    // Delete book
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/";
    }

    // Update book
    @PostMapping("/update")
    public String updateBook(@Validated @ModelAttribute("book") Book book) {
        bookRepository.save(book);
        return "redirect:/";
    }
}
