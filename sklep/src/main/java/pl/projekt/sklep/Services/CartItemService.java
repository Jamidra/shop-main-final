package pl.projekt.sklep.Services;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.Models.Cart;
import pl.projekt.sklep.Models.CartItem;
import pl.projekt.sklep.Models.Item;
import pl.projekt.sklep.Repositories.CartItemRepository;
import pl.projekt.sklep.Repositories.CartRepository;

import java.math.BigDecimal;

@Service
public class CartItemService  implements CartItemServiceInterface{
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemServiceInterface itemService;
    private final CartServiceInterface cartService;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ItemServiceInterface itemService, CartServiceInterface cartService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long itemId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Item item = itemService.getItemById(itemId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(cartItem1 -> cartItem1.getItem().getItemId().equals(itemId))
                .findFirst()
                .orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setItem(item);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(item.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, itemId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long itemId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getItem().getItemId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setPrice(item.getItem().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream().map(CartItem ::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public CartItem getCartItem(Long cartId, Long itemId) {
        Cart cart = cartService.getCart(cartId);
        return  cart.getItems()
                .stream()
                .filter(item -> item.getItem().getItemId().equals(itemId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}

