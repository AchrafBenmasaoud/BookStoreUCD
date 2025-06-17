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

    // Get All Books
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

    // Create a new Book
    @PostMapping()
    public String newAuthor(@Validated @ModelAttribute("author") Author author)
    {
      authorRepository.save(author);
      return "redirect:/author";
    }

//    // Get a Single Book
//    @GetMapping("/authors/{id}")
//    public String getAuthorById(@PathVariable(value = "id") Long authorId, Model model) throws AuthorNotFoundException {
//        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
//        model.addAttribute("author", author);
//        return "editAuthor";
//
//    }
//
//    // Update an Existing Book
//    @PutMapping("/save_author")
//    public String updateAuthor(@ModelAttribute("author") Author author, Model model)
//            throws BookNotFoundException{
//        authorRepository.save(author);
//        return "redirect:/authors";
//    }
//
//    // Delete a Book
//    @DeleteMapping("/delete_author/{id}")
//    public String deleteAuthor(@PathVariable(value = "id") Long authorId, Model model) throws AuthorNotFoundException {
//        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
//        authorRepository.delete(author);
//        return "redirect:/authors";
//    }



}
