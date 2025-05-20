package pl.projekt.sklep.Services;

import pl.projekt.sklep.Models.Cart;

import java.math.BigDecimal;

public interface CartServiceInterface {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();

    Cart getCartByUserId(Long userId);
}
