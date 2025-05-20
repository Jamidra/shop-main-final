package pl.projekt.sklep.Services;

import pl.projekt.sklep.reqs.AddItemDto;
import pl.projekt.sklep.reqs.ItemUpdateRequest;
import pl.projekt.sklep.Dtos.ItemDto;
import pl.projekt.sklep.Models.Item;

import java.util.List;

public interface ItemServiceInterface {
    Item addItem(Item request);

    Item addItem(AddItemDto request);

    Item getItemById(Long id);
    void deleteItemById(Long id);
    Item updateItem(ItemUpdateRequest item, Long itemId);

    List<Item> getAllItems();
    List<Item> getItemsByCategory(String category);
    List<Item> getItemsByName(String name);
    Long countItemsByName(String name);

    List<ItemDto> getConvertedItems(List<Item> items);

    ItemDto convertToDto(Item item);
}
