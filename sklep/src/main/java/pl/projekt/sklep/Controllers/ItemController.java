package pl.projekt.sklep.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.Dtos.ItemDto;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.Models.Item;
import pl.projekt.sklep.Services.ItemService;
import pl.projekt.sklep.reqs.AddItemDto;
import pl.projekt.sklep.reqs.ItemUpdateRequest;
import pl.projekt.sklep.responses.ApiResponse;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/products")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("item/{itemId}/item")
    public ResponseEntity<List<Item>> getItemById(@PathVariable Long itemId) {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/add")
    public ResponseEntity<Item> addItem(@RequestBody AddItemDto request) {
        Item items = itemService.addItem(request);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{itemId}/update")
    public  ResponseEntity<String> updateItem(@RequestBody ItemUpdateRequest request, @PathVariable Long itemId) {
        try {
            Item theItem = itemService.updateItem(request, itemId);
            ItemDto itemDto = itemService.convertToDto(theItem);
            return ResponseEntity.ok("Update item success!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body("Item not found!");
        }
    }

    @DeleteMapping("/{itemId}/delete")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        try {
            itemService.deleteItemById(itemId);
            return ResponseEntity.ok("Delete item success!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body("Item not found");
        }
    }

    @GetMapping("/{name}/items")
    public ResponseEntity<Object> getItemByName(@PathVariable String name){
        try {
            List<Item> items = itemService.getItemsByName(name);
            if (items.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body("No items found ");
            }
            List<ItemDto> convertedItems = itemService.getConvertedItems(items);
            return  ResponseEntity.ok(new ApiResponse("success", convertedItems));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @GetMapping("/{category}/all/items")
    public ResponseEntity<Object> findItemByCategory(@PathVariable String category) {
        try {
            List<Item> items = itemService.getItemsByCategory(category);
            if (items.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body("No items found ");
            }
            List<ItemDto> convertedItems = itemService.getConvertedItems(items);
            return  ResponseEntity.ok(new ApiResponse("success", convertedItems));
        } catch (Exception e) {
            return ResponseEntity.ok("Exception occurred");
        }
    }

    @GetMapping("/item/count/by-name")
    public ResponseEntity<Object> countItemsByName(@RequestParam String name) {
        try {
            var itemCount = itemService.countItemsByName(name);
            return ResponseEntity.ok(new ApiResponse("Product count!", itemCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

}
