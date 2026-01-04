package ro.unibuc.libra.librarymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.service.api.BookService;
import ro.unibuc.libra.librarymanagement.util.enums.BookCategory;

import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.BOOKS;

@RestController
@RequestMapping(BOOKS)
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        var response = bookService.getAllBooks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> fetchBook(@PathVariable("id") Long id) {
        var response = bookService.findBookById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        var response = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") Long id, @RequestBody BookDTO bookDTO) {
        var response = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable("id") Long id) {
        var response = bookService.deleteBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchByTitle(@RequestParam("title") String title) {
        var response = bookService.searchByTitle(title);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<BookDTO>> findByCategory(@PathVariable("category") BookCategory category) {
        var response = bookService.findByCategory(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookDTO>> findAvailableBooks() {
        var response = bookService.findAvailableBooks();
        return ResponseEntity.ok(response);
    }
}
