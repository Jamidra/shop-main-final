package pl.projekt.sklep.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.Services.CartItemServiceInterface;
import pl.projekt.sklep.Services.CartServiceInterface;


import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("api/cartItems")
@Tag(name = "Cart Item Controller", description = "API for managing items in a cart")
public class CartItemController {
    private final CartItemServiceInterface cartItemService;
    private final CartServiceInterface cartService;

    @Autowired
    public CartItemController(CartItemServiceInterface cartItemService, CartServiceInterface cartService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @Operation(summary = "Add item to cart", description = "Adds an item to a cart or creates a new cart if none is specified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added to cart successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Item or cart not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/item/add")
    public ResponseEntity<Map<String, Object>> addItemToCart(
            @Parameter(description = "ID of the cart (optional, creates new cart if not provided)") @RequestParam(required = false) Long cartId,
            @Parameter(description = "ID of the item to add", required = true) @RequestParam Long itemId,
            @Parameter(description = "Quantity of the item to add", required = true) @RequestParam Integer quantity) {
        try {
            if (cartId == null) {
                cartId = cartService.initializeNewCart();
            }
            cartItemService.addItemToCart(cartId, itemId, quantity);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Item added to cart successfully");
            response.put("data", Map.of("cartId:", cartId));
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Remove item from cart", description = "Removes an item from a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed from cart successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Cart or item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<Map<String, Object>> removeItemFromCart(
            @Parameter(description = "ID of the cart", required = true) @PathVariable Long cartId,
            @Parameter(description = "ID of the item to remove", required = true) @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Item removed from cart successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Update item quantity in cart", description = "Updates the quantity of an item in a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item quantity updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Cart or item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<Map<String, Object>> updateItemQuantity(
            @Parameter(description = "ID of the cart", required = true) @PathVariable Long cartId,
            @Parameter(description = "ID of the item to update", required = true) @PathVariable Long itemId,
            @Parameter(description = "New quantity for the item", required = true) @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Item quantity updated successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }
}

