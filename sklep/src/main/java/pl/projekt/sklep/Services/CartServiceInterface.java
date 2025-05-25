package pl.projekt.sklep.Services;

import pl.projekt.sklep.Models.Cart;

import java.math.BigDecimal;

public interface CartServiceInterface {
    Cart getCart(Long cartId);
    void clearCart(Long cartId);
    BigDecimal getTotalPrice(Long cartId);

    Long initializeNewCart();

    Cart getCartByCartId(Long cartId);
}
