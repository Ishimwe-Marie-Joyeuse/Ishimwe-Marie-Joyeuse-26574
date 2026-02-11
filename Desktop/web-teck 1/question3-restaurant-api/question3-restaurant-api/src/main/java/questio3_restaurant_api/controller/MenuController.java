package questio3_restaurant_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import questio3_restaurant_api.MenuItem;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    // In-memory list to store menu items (simulating a database)
    private List<MenuItem> menuItems = new ArrayList<>();
    private Long nextId = 9L;

    // Initialize with 8 different menu items across all categories
    public MenuController() {
        // Appetizers
        menuItems.add(new MenuItem(1L, "Caesar Salad", "Fresh romaine lettuce with parmesan and croutons", 8.99, "Appetizer", true));
        menuItems.add(new MenuItem(2L, "Buffalo Wings", "Spicy chicken wings served with blue cheese dip", 12.99, "Appetizer", true));
        
        // Main Course
        menuItems.add(new MenuItem(3L, "Grilled Salmon", "Atlantic salmon with lemon butter sauce and vegetables", 24.99, "Main Course", true));
        menuItems.add(new MenuItem(4L, "Ribeye Steak", "12oz premium ribeye steak with mashed potatoes", 32.99, "Main Course", true));
        menuItems.add(new MenuItem(5L, "Vegetarian Pasta", "Penne pasta with fresh vegetables in marinara sauce", 16.99, "Main Course", false));
        
        // Desserts
        menuItems.add(new MenuItem(6L, "Chocolate Lava Cake", "Warm chocolate cake with vanilla ice cream", 7.99, "Dessert", true));
        menuItems.add(new MenuItem(7L, "Tiramisu", "Classic Italian dessert with coffee and mascarpone", 6.99, "Dessert", true));
        
        // Beverages
        menuItems.add(new MenuItem(8L, "Fresh Lemonade", "Homemade lemonade with mint", 3.99, "Beverage", true));
    }

    /**
     * GET /api/menu - Get all menu items
     * @return List of all menu items with HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItems);
    }

    /**
     * GET /api/menu/{id} - Get specific menu item
     * @param id - Menu item ID
     * @return Menu item with HTTP 200 OK or HTTP 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        Optional<MenuItem> menuItem = menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
        
        if (menuItem.isPresent()) {
            return ResponseEntity.ok(menuItem.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET /api/menu/category/{category} - Get items by category
     * @param category - Category name (Appetizer, Main Course, Dessert, Beverage)
     * @return List of menu items in the specified category with HTTP 200 OK
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItem> itemsByCategory = menuItems.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .toList();
        
        return ResponseEntity.ok(itemsByCategory);
    }

    /**
     * GET /api/menu/available - Get only available items
     * @param available - Filter by availability (true/false)
     * @return List of available/unavailable menu items with HTTP 200 OK
     */
    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableMenuItems(@RequestParam boolean available) {
        List<MenuItem> availableItems = menuItems.stream()
                .filter(item -> item.isAvailable() == available)
                .toList();
        
        return ResponseEntity.ok(availableItems);
    }

    /**
     * GET /api/menu/search?name={name} - Search menu items by name
     * @param name - Name to search for (case-insensitive, partial match)
     * @return List of matching menu items with HTTP 200 OK
     */
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItemsByName(@RequestParam String name) {
        List<MenuItem> matchingItems = menuItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        
        return ResponseEntity.ok(matchingItems);
    }

    /**
     * POST /api/menu - Add new menu item
     * @param menuItem - Menu item object from request body
     * @return Created menu item with HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        menuItem.setId(nextId++);
        menuItems.add(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }

    /**
     * PUT /api/menu/{id}/availability - Toggle item availability
     * @param id - Menu item ID
     * @return Updated menu item with HTTP 200 OK or HTTP 404 Not Found
     */
    @PutMapping("/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id) {
        Optional<MenuItem> menuItem = menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
        
        if (menuItem.isPresent()) {
            MenuItem item = menuItem.get();
            item.setAvailable(!item.isAvailable()); // Toggle availability
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * DELETE /api/menu/{id} - Remove menu item
     * @param id - Menu item ID to delete
     * @return HTTP 204 No Content if deleted, HTTP 404 Not Found if not exists
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        boolean removed = menuItems.removeIf(item -> item.getId().equals(id));
        
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}