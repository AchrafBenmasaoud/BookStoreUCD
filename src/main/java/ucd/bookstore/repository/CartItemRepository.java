package ucd.bookstore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucd.bookstore.model.Cart;
import ucd.bookstore.model.CartItem;
import ucd.bookstore.model.User;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findById(Long id);
}
