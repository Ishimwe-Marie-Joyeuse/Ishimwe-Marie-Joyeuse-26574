package questionBonus_userprofile_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import questionBonus_userprofile_api.ApiResponse;
import questionBonus_userprofile_api.UserProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    // In-memory list to store user profiles (simulating a database)
    private List<UserProfile> users = new ArrayList<>();
    private Long nextId = 8L;

    // Initialize with sample user profiles
    public UserProfileController() {
        users.add(new UserProfile(1L, "john_doe", "john@example.com", "John Doe", 
                28, "USA", "Software developer passionate about Spring Boot", true));
        users.add(new UserProfile(2L, "jane_smith", "jane@example.com", "Jane Smith", 
                25, "Canada", "Full-stack developer and tech enthusiast", true));
        users.add(new UserProfile(3L, "mike_wilson", "mike@example.com", "Mike Wilson", 
                32, "UK", "DevOps engineer with cloud expertise", true));
        users.add(new UserProfile(4L, "sarah_jones", "sarah@example.com", "Sarah Jones", 
                22, "Australia", "Frontend developer specializing in React", false));
        users.add(new UserProfile(5L, "david_lee", "david@example.com", "David Lee", 
                35, "USA", "Senior backend engineer and team lead", true));
        users.add(new UserProfile(6L, "emma_brown", "emma@example.com", "Emma Brown", 
                27, "Germany", "UX designer and creative thinker", true));
        users.add(new UserProfile(7L, "carlos_garcia", "carlos@example.com", "Carlos Garcia", 
                30, "Spain", "Mobile app developer for iOS and Android", false));
    }

    /**
     * GET /api/users - Get all user profiles
     * @return ApiResponse with list of all users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllUsers() {
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true,
                "User profiles retrieved successfully",
                users
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/{userId} - Get user profile by ID
     * @param userId - User ID
     * @return ApiResponse with user data or error message
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getUserById(@PathVariable Long userId) {
        Optional<UserProfile> user = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
        
        if (user.isPresent()) {
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    true,
                    "User profile found",
                    user.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    false,
                    "User profile not found with ID: " + userId,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/users/search/username/{username} - Search user by username
     * @param username - Username to search
     * @return ApiResponse with user data or error message
     */
    @GetMapping("/search/username/{username}")
    public ResponseEntity<ApiResponse<UserProfile>> getUserByUsername(@PathVariable String username) {
        Optional<UserProfile> user = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
        
        if (user.isPresent()) {
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    true,
                    "User found with username: " + username,
                    user.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    false,
                    "No user found with username: " + username,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/users/search/country/{country} - Get users by country
     * @param country - Country name
     * @return ApiResponse with list of users from that country
     */
    @GetMapping("/search/country/{country}")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getUsersByCountry(@PathVariable String country) {
        List<UserProfile> countryUsers = users.stream()
                .filter(u -> u.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
        
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true,
                "Found " + countryUsers.size() + " user(s) from " + country,
                countryUsers
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/search/age-range?min={min}&max={max} - Get users by age range
     * @param min - Minimum age
     * @param max - Maximum age
     * @return ApiResponse with list of users in age range
     */
    @GetMapping("/search/age-range")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getUsersByAgeRange(
            @RequestParam int min,
            @RequestParam int max) {
        
        List<UserProfile> ageRangeUsers = users.stream()
                .filter(u -> u.getAge() >= min && u.getAge() <= max)
                .collect(Collectors.toList());
        
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true,
                "Found " + ageRangeUsers.size() + " user(s) aged between " + min + " and " + max,
                ageRangeUsers
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/active - Get active users only
     * @return ApiResponse with list of active users
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getActiveUsers() {
        List<UserProfile> activeUsers = users.stream()
                .filter(UserProfile::isActive)
                .collect(Collectors.toList());
        
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true,
                "Found " + activeUsers.size() + " active user(s)",
                activeUsers
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/inactive - Get inactive users only
     * @return ApiResponse with list of inactive users
     */
    @GetMapping("/inactive")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getInactiveUsers() {
        List<UserProfile> inactiveUsers = users.stream()
                .filter(u -> !u.isActive())
                .collect(Collectors.toList());
        
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true,
                "Found " + inactiveUsers.size() + " inactive user(s)",
                inactiveUsers
        );
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/users - Create new user profile
     * @param user - User profile object from request body
     * @return ApiResponse with created user data
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createUser(@RequestBody UserProfile user) {
        user.setUserId(nextId++);
        users.add(user);
        
        ApiResponse<UserProfile> response = new ApiResponse<>(
                true,
                "User profile created successfully",
                user
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/users/{userId} - Update user profile
     * @param userId - User ID to update
     * @param updatedUser - Updated user information
     * @return ApiResponse with updated user data or error message
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserProfile updatedUser) {
        
        Optional<UserProfile> existingUser = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
        
        if (existingUser.isPresent()) {
            UserProfile user = existingUser.get();
            
            // Update user fields
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setFullName(updatedUser.getFullName());
            user.setAge(updatedUser.getAge());
            user.setCountry(updatedUser.getCountry());
            user.setBio(updatedUser.getBio());
            user.setActive(updatedUser.isActive());
            
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    true,
                    "User profile updated successfully",
                    user
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    false,
                    "User profile not found with ID: " + userId,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * PATCH /api/users/{userId}/activate - Activate user profile
     * @param userId - User ID to activate
     * @return ApiResponse with updated user data or error message
     */
    @PatchMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<UserProfile>> activateUser(@PathVariable Long userId) {
        Optional<UserProfile> existingUser = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
        
        if (existingUser.isPresent()) {
            UserProfile user = existingUser.get();
            user.setActive(true);
            
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    true,
                    "User profile activated successfully",
                    user
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    false,
                    "User profile not found with ID: " + userId,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * PATCH /api/users/{userId}/deactivate - Deactivate user profile
     * @param userId - User ID to deactivate
     * @return ApiResponse with updated user data or error message
     */
    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserProfile>> deactivateUser(@PathVariable Long userId) {
        Optional<UserProfile> existingUser = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
        
        if (existingUser.isPresent()) {
            UserProfile user = existingUser.get();
            user.setActive(false);
            
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    true,
                    "User profile deactivated successfully",
                    user
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    false,
                    "User profile not found with ID: " + userId,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * DELETE /api/users/{userId} - Delete user profile
     * @param userId - User ID to delete
     * @return ApiResponse with success or error message
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        boolean removed = users.removeIf(u -> u.getUserId().equals(userId));
        
        if (removed) {
            ApiResponse<Void> response = new ApiResponse<>(
                    true,
                    "User profile deleted successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    "User profile not found with ID: " + userId,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}