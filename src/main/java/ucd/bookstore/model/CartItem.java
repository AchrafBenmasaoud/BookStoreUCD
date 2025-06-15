package ucd.bookstore.model;


import jakarta.persistence.*;

@Entity
@Table(name = "cartitem")
public class CartItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Cart cart;
    @ManyToOne
    private Book book;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Cart cart, Book book, int quantity) {
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
