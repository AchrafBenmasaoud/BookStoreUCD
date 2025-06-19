package ucd.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ucd.bookstore.model.Book;
import ucd.bookstore.repository.BookRepository;

import java.util.*;

@Controller
@RequestMapping()
public class HomeController {

    @Autowired
    private BookRepository repository;

    // Get All Books
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", repository.findAllByOrderByCopiesDesc());
        return "index";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam("query") String query, Model model) {

        Set<Book> results = new HashSet<>();
        results.addAll(repository.findByTitleContainingIgnoreCase(query));
        results.addAll(repository.findByIsbnContainingIgnoreCase(query));
        results.addAll(repository.findByAuthors_AuthorNameContainingIgnoreCase(query));
        results.addAll(repository.findByAuthors_AuthorSurnameContainingIgnoreCase(query));

        List<Book> sortedResults = new ArrayList<>(results);

        // Sort: available books (copies > 0) first
        sortedResults.sort(Comparator.comparingInt(Book::getCopies).reversed());

        model.addAttribute("books", sortedResults);
        return "index";
    }
}
