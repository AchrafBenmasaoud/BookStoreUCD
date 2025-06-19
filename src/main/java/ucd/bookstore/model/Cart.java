package ucd.bookstore.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart() {
    }

    public Cart(User user) {
        this.user = user;
        if (user.getCart() != this) {
            user.setCart(this);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user.getCart() != this) {
            user.setCart(this);
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
