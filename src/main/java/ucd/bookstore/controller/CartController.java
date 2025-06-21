package ucd.bookstore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    private BookRepository bookRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping("/add")
    public String addToCart(@RequestParam("bookId") Long bookId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // 1. Get the logged-in user
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        // 2. Get or create the cart
        Cart cart = cartRepository.findByUser(user);

        // 3. Get the book
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            redirectAttributes.addFlashAttribute("error", "Book not found.");
            return "redirect:/cart";
        }

        // 4. Get current quantity in cart
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        int currentQuantity = existingItem.map(CartItem::getQuantity).orElse(0);
        int availableCopies = book.getCopies();

        if (currentQuantity + 1 > availableCopies) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot add more than " + availableCopies + " copies of \"" + book.getTitle() + "\" to your cart.");
            return "redirect:/cart";
        }

        // 5. Add or update cart item
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(currentQuantity + 1);
        } else {
            CartItem newItem = new CartItem();
            newItem.setBook(book);
            newItem.setQuantity(1);
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
        }

        // 6. Save the cart
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
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/auth/login";

        Optional<CartItem> cartItemOpt = cartItemRepository.findById(itemId);

        if (cartItemOpt.isPresent()) {
            CartItem item = cartItemOpt.get();

            if (!item.getCart().getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to update this item.");
                return "redirect:/cart";
            }

            int availableCopies = item.getBook().getCopies();
            if (quantity > availableCopies) {
                redirectAttributes.addFlashAttribute("error",
                        "Only " + availableCopies + " copies available for \"" + item.getBook().getTitle() + "\".");
                return "redirect:/cart";
            }

            if (quantity > 0) {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }

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