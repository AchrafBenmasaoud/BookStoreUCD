package ucd.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ucd.bookstore.repository.BookRepository;

@Controller
@RequestMapping()
public class HomeController {

    @Autowired
    private BookRepository repository;

    // Get All Books
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", repository.findAll());
        return "index";
    }
}
