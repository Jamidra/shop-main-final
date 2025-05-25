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
import pl.projekt.sklep.Exceptions.AlreadyExistsException;
import pl.projekt.sklep.Exceptions.ResourceNotFoundException;
import pl.projekt.sklep.Models.Category;
import pl.projekt.sklep.Services.CategoryServiceInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("api/categories")
@Tag(name = "Category Controller", description = "API for managing categories in the store")
public class CategoryController {
    private final CategoryServiceInterface categoryService;

    public CategoryController(CategoryServiceInterface categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", categories);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add a new category", description = "Creates a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "409", description = "Category already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCategory(
            @Parameter(description = "Category details to add", required = true) @RequestBody Category name) {
        try {
            Category theCategory = categoryService.addCategory(name);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Category created successfully");
            response.put("data", theCategory);
            return ResponseEntity.ok(response);
        } catch (AlreadyExistsException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(CONFLICT).body(response);
        }
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(
            @Parameter(description = "ID of the category to retrieve", required = true) @PathVariable Long id) {
        try {
            Category theCategory = categoryService.getCategoryById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", theCategory);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Get category by name", description = "Retrieves a category by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Map<String, Object>> getCategoryByName(
            @Parameter(description = "Name of the category to retrieve", required = true) @PathVariable String name) {
        try {
            Category theCategory = categoryService.getCategoryByName(name);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", theCategory);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Map<String, Object>> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true) @PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Category deleted successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Update a category", description = "Updates an existing category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @PutMapping("/{id}/update")
    public ResponseEntity<Map<String, Object>> updateCategory(
            @Parameter(description = "ID of the category to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated category details", required = true) @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Category updated successfully");
            response.put("data", updatedCategory);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }
}

