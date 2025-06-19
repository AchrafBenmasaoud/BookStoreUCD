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
import ucd.bookstore.repository.AuthorRepository;
import ucd.bookstore.repository.BookRepository;
import ucd.bookstore.repository.CartItemRepository;
import ucd.bookstore.repository.CartRepository;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping("/add")
    public String addToCart(@RequestParam("bookId") Long bookId, HttpSession session) {
        // 1. Get the logged-in user from the session
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        // 2. Get or create the cart
        Cart cart = cartRepository.findByUser(user);

        // 3. Get the book
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            return "redirect:/cart?error=bookNotFound";
        }

        // 4. Add or update cart item
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            CartItem newItem = new CartItem();
            newItem.setBook(book);
            newItem.setQuantity(1);
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
        }

        // 5. Save the cart
        cartRepository.save(cart);

        return "redirect:/cart";
    }

    @GetMapping()
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getCartItems().isEmpty()) {
            model.addAttribute("empty", true);
            return "cart";
        }

        model.addAttribute("cartItems", cart.getCartItems());

        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("total", total);

        return "cart";
    }
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long itemId,
                                 @RequestParam int quantity,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/auth/login";

        Optional<CartItem> cartItemOpt = cartItemRepository.findById(itemId);
        cartItemOpt.ifPresent(item -> {
            if (quantity > 0 && item.getCart().getUser().getId().equals(user.getId())) {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        });

        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeCartItem(@RequestParam Long itemId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/auth/login";

        Optional<CartItem> cartItemOpt = cartItemRepository.findById(itemId);
        cartItemOpt.ifPresent(item -> {
            if (item.getCart().getUser().getId().equals(user.getId())) {
                cartItemRepository.delete(item);
            }
        });

        return "redirect:/cart";
    }
}