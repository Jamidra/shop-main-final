package pl.projekt.sklep.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.Dtos.ItemDto;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.Models.Item;
import pl.projekt.sklep.Services.ItemService;
import pl.projekt.sklep.Services.ItemServiceInterface;
import pl.projekt.sklep.reqs.AddItemDto;
import pl.projekt.sklep.reqs.ItemUpdateRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Item Controller", description = "API for managing items in the store")
public class ItemController {
    private final ItemServiceInterface itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Get all items", description = "Retrieves a list of all items in the store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", items);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get item by ID", description = "Retrieves a single item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved item",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("item/{itemId}/item")
    public ResponseEntity<Map<String, Object>> getItemById(
            @Parameter(description = "ID of the item to retrieve", required = true) @PathVariable Long itemId) {
        try {
            Item item = itemService.getItemById(itemId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", item);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }


    @Operation(summary = "Update an item", description = "Updates an existing item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @PutMapping("/{itemId}/update")
    public ResponseEntity<Map<String, Object>> updateItem(
            @Parameter(description = "Updated item details", required = true) @RequestBody ItemUpdateRequest request,
            @Parameter(description = "ID of the item to update", required = true) @PathVariable Long itemId) {
        try {
            Item theItem = itemService.updateItem(request, itemId);
            ItemDto itemDto = itemService.convertToDto(theItem);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Item updated successfully");
            response.put("data", itemDto);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Item not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Delete an item", description = "Deletes an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @DeleteMapping("/{itemId}/delete")
    public ResponseEntity<Map<String, Object>> deleteItem(
            @Parameter(description = "ID of the item to delete", required = true) @PathVariable Long itemId) {
        try {
            itemService.deleteItemById(itemId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Item deleted successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Item not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Get items by name", description = "Retrieves a list of items by their name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "No items found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{name}/items")
    public ResponseEntity<Map<String, Object>> getItemByName(
            @Parameter(description = "Name of the item to search for", required = true) @PathVariable String name) {
        try {
            List<Item> items = itemService.getItemsByName(name);
            return getMapResponseEntity(items);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "An error occurred");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(List<Item> items) {
        Map<String, Object> response = new HashMap<>();
        if (items.isEmpty()) {
            response.put("status", "error");
            response.put("message", "No items found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
        List<ItemDto> convertedItems = itemService.getConvertedItems(items);
        response.put("status", "success");
        response.put("data", convertedItems);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get items by category", description = "Retrieves a list of items by their category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "No items found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{category}/all/items")
    public ResponseEntity<Map<String, Object>> findItemByCategory(
            @Parameter(description = "Category of the items to retrieve", required = true) @PathVariable String category) {
        try {
            List<Item> items = itemService.getItemsByCategory(category);
            return getMapResponseEntity(items);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "An error occurred");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Count items by name", description = "Counts the number of items with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully counted items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "applications/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/item/count/by-name")
    public ResponseEntity<Map<String, Object>> countItemsByName(
            @Parameter(description = "Name of the items to count", required = true) @RequestParam String name) {
        try {
            var itemCount = itemService.countItemsByName(name);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", itemCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
