package ucd.bookstore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucd.bookstore.model.Cart;
import ucd.bookstore.model.User;
import ucd.bookstore.model.UserRole;
import ucd.bookstore.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLogInForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            Model model) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                // Hash the entered password
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                String hashedInputPassword = HexFormat.of().formatHex(hash);

                // Compare the stored hashed password with the hashed input
                if (hashedInputPassword.equals(user.getPassword())) {
                    session.setAttribute("loggedInUser", user);
                    return "redirect:/";
                }

            } catch (NoSuchAlgorithmException e) {
                // Ideally log the exception
                e.printStackTrace();
            }
        }

        model.addAttribute("error", true);
        return "login";
    }

    // Create a new User
    @PostMapping("/register")
    public String saveUser(@Validated @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        // Hash the password using SHA-256
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
            String hashedPassword = HexFormat.of().formatHex(hash);
            user.setPassword(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // Ideally log this properly or handle as needed
            throw new RuntimeException("Error hashing password", e);
        }

        // Create and assign a cart
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        user.setRole(UserRole.CUSTOMER); // for our admin/customers

        userRepository.save(user);
        return "redirect:/auth/login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
