package questio1_library_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import questio1_library_api.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    // In-memory list to store books (simulating a database)
    private List<Book> books = new ArrayList<>();
    private Long nextId = 4L;

    // Initialize with 3 sample books
    public BookController() {
        books.add(new Book(1L, "Clean Code", "Robert Martin", "978-0132350884", 2008));
        books.add(new Book(2L, "Effective Java", "Joshua Bloch", "978-0134685991", 2018));
        books.add(new Book(3L, "Spring in Action", "Craig Walls", "978-1617294945", 2018));
    }

    /**
     * GET /api/books - Return a list of all books
     * @return List of all books with HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/{id} - Return a specific book by ID
     * @param id - Book ID
     * @return Book object with HTTP 200 OK or HTTP 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
        
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET /api/books/search?title={title} - Search books by title
     * @param title - Title to search for (case-insensitive, partial match)
     * @return List of matching books with HTTP 200 OK
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        List<Book> matchingBooks = books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
        
        return ResponseEntity.ok(matchingBooks);
    }

    /**
     * POST /api/books - Add a new book
     * @param book - Book object from request body
     * @return Created book with HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        book.setId(nextId++);
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    /**
     * DELETE /api/books/{id} - Delete a book by ID
     * @param id - Book ID to delete
     * @return HTTP 204 No Content if deleted, HTTP 404 Not Found if not exists
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean removed = books.removeIf(b -> b.getId().equals(id));
        
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}