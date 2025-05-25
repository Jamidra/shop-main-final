package pl.projekt.sklep.Services;

import pl.projekt.sklep.Models.CartItem;

public interface CartItemServiceInterface {
    void addItemToCart(Long cartId, Long itemId, int quantity);
    void removeItemFromCart(Long cartId, Long itemId);
    void updateItemQuantity(Long cartId, Long itemId, int quantity);

    CartItem getCartItem(Long cartId, Long itemId);
}
