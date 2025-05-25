package pl.projekt.sklep.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.Models.Cart;
import pl.projekt.sklep.Services.CartServiceInterface;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("api/cart")
@Tag(name = "Cart Controller", description = "API for managing shopping carts")
public class CartController {
    private final CartServiceInterface cartService;

    public CartController(CartServiceInterface cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Get cart by ID", description = "Retrieves a cart by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{cartId}")
    public ResponseEntity<Map<String, Object>> getCart(
            @Parameter(description = "ID of the cart to retrieve", required = true) @PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", cart);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Clear a cart", description = "Removes all items from a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Map<String, Object>> clearCart(
            @Parameter(description = "ID of the cart to clear", required = true) @PathVariable Long cartId) {
        try {
            cartService.clearCart(cartId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cart cleared successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Get total price of cart", description = "Calculates the total price of items in a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total price retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<Map<String, Object>> getTotalAmount(
            @Parameter(description = "ID of the cart to calculate total price", required = true) @PathVariable Long cartId) {
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", totalPrice);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }
}
