package pl.projekt.sklep.Services;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.Models.Cart;
import pl.projekt.sklep.Repositories.CartItemRepository;
import pl.projekt.sklep.Repositories.CartRepository;

import java.math.BigDecimal;

@Service
public class CartService implements CartServiceInterface {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    @Override
    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Transactional
    @Override
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cartItemRepository.deleteAllById(cartId);
        cart.getItems().clear();
        cartRepository.deleteById(cartId);
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCart(cartId);
        return cart.getTotalAmount();
    }

    @Transactional
    @Override
    public Long initializeNewCart() {
        int retries = 3;
        while (retries > 0) {
            try {
                Cart newCart = new Cart();
                return cartRepository.save(newCart).getCartId();
            } catch (ObjectOptimisticLockingFailureException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("Failed to create cart after retries", e);
                }
            }
        }
        throw new RuntimeException("Failed to create cart");
    }

    @Override
    public Cart getCartByCartId(Long cartId) {
        return cartRepository.findById(cartId)
                .orElse(null); // Or throw exception if null is not acceptable
    }
}
