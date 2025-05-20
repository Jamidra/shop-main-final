package pl.projekt.sklep.Services;

import pl.projekt.sklep.Models.CartItem;

public interface CartItemServiceInterface {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
