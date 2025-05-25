package pl.projekt.sklep.Services;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.projekt.sklep.Repositories.CategoryRepository;
import pl.projekt.sklep.Repositories.ItemRepository;
import pl.projekt.sklep.Dtos.ItemDto;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.reqs.ItemUpdateRequest;
import pl.projekt.sklep.Models.Category;
import pl.projekt.sklep.Models.Item;
import pl.projekt.sklep.reqs.AddItemDto;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService implements ItemServiceInterface {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;


    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;

    }

    @Override
    public Item addItem(Item request) {
        return null;
    }

    @Override
    public Item addItem(AddItemDto request) {

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return itemRepository.save(createItem(request, category));
    }

    private Item createItem(AddItemDto request, Category category) {
        Item item = new Item();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setInventory(request.getInventory());
        item.setDescription(request.getDescription());
        item.setCategory(category);
        return item;
    }


    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Item not found!"));
    }

    @Transactional
    public void deleteItemById(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ResourceNotFoundException("Item not found!");
        }
        try {
            itemRepository.delete(item.get());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new ResourceNotFoundException("Cannot delete item with ID " + itemId + " because it is referenced by other records");
        }
    }

    @Override
    public Item updateItem(ItemUpdateRequest request, Long itemId) {
        return itemRepository.findById(itemId)
                .map(existingItem -> updateExistingItem(existingItem,request))
                .map(itemRepository :: save)
                .orElseThrow(()-> new ResourceNotFoundException("Item not found!"));
    }

    private Item updateExistingItem(Item existingItem, ItemUpdateRequest request) {
        existingItem.setName(request.getName());
        existingItem.setPrice(request.getPrice());
        existingItem.setInventory(request.getInventory());
        existingItem.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingItem.setCategory(category);
        return  existingItem;

    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findByCategoryName(category);
    }

    @Override
    public List<Item> getItemsByName(String name) {
        return itemRepository.findByName(name);
    }


    public Long countItemsByName(String name) {
        return itemRepository.countByName(name);
    }

    @Override
    public List<ItemDto> getConvertedItems(List<Item> items) {
        return items.stream().map(this::convertToDto).toList();
    }

    @Override
    public ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(item.getItemId());
        itemDto.setName(item.getName());
        itemDto.setPrice(item.getPrice());
        itemDto.setInventory(item.getInventory());
        itemDto.setDescription(item.getDescription());
        itemDto.setCategory(item.getCategory());
        return itemDto;
    }
}
