package ucd.bookstore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ucd.bookstore.model.Book;
import ucd.bookstore.model.Cart;
import ucd.bookstore.model.CartItem;
import ucd.bookstore.model.User;
import ucd.bookstore.repository.BookRepository;
import ucd.bookstore.repository.CartItemRepository;
import ucd.bookstore.repository.CartRepository;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        return "checkout";
    }

    @PostMapping("/confirmed")
    public String processCheckout(@RequestParam String name,
                                  @RequestParam String cardNumber,
                                  @RequestParam String expiry,
                                  @RequestParam String cvv,
                                  Model model,
                                  HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Cart cart = cartRepository.findByUser(user);
        List<CartItem> cartItems = cart.getCartItems();

        // Check availability
        for (CartItem item : cartItems) {
            Book book = item.getBook();
            int available = book.getCopies();
            int requested = item.getQuantity();

            if (requested > available) {
                model.addAttribute("error", "Not enough copies available for '" + book.getTitle() + "'.");
                return "checkout";
            }
        }

        // All items are available – update inventory
        for (CartItem item : cartItems) {
            Book book = item.getBook();
            book.setCopies(book.getCopies() - item.getQuantity());
            bookRepository.save(book);
        }

        // Clear the cart
        cartItemRepository.deleteAll(cartItems);
        cart.getCartItems().clear();
        cartRepository.save(cart);

        model.addAttribute("success", "Thank you! Your order has been placed successfully.");
        return "checkout";
    }
}
