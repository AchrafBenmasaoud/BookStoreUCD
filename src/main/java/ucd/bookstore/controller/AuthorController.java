package ucd.bookstore.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucd.bookstore.model.Author;
import ucd.bookstore.repository.AuthorRepository;

@Controller
@RequestMapping("/author") // default endpoint for controller
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    // Get All QAuthor
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

    // Create a new Author
    @PostMapping()
    public String newAuthor(@Validated @ModelAttribute("author") Author author)
    {
      authorRepository.save(author);
      return "redirect:/author";
    }

    // OPTIONAL: Delete author
    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorRepository.deleteById(id);
        return "redirect:/author";
    }

}
