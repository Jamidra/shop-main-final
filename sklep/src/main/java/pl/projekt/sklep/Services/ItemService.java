package pl.projekt.sklep.Services;


import org.springframework.stereotype.Service;
import pl.projekt.sklep.Repositories.CategoryRepository;
import pl.projekt.sklep.Repositories.ItemRepository;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
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
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Item not found!"));
    }

    @Override
    public void deleteItemById(Long id) {
        itemRepository.findById(id)
                .ifPresentOrElse(itemRepository::delete,
                        () -> {throw new ResourceNotFoundException("Item not found!");});
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
        return modelMapper.map(item, ItemDto.class);
    }
}
