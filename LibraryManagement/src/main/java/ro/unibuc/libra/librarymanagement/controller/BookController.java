package ro.unibuc.libra.librarymanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.service.api.BookService;
import ro.unibuc.libra.librarymanagement.util.enums.BookCategory;

import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.BOOKS;

@Tag(name = "Books")
@RestController
@RequestMapping(BOOKS)
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        var response = bookService.getAllBooks();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get book by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> fetchBook(@PathVariable("id") Long id) {
        var response = bookService.findBookById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create book")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404")
    })
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        var response = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update book")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") Long id, @RequestBody BookDTO bookDTO) {
        var response = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete book")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable("id") Long id) {
        var response = bookService.deleteBook(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search books by title")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchByTitle(@RequestParam("title") String title) {
        var response = bookService.searchByTitle(title);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get books by category")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<BookDTO>> findByCategory(@PathVariable("category") BookCategory category) {
        var response = bookService.findByCategory(category);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get available books")
    @ApiResponses({ @ApiResponse(responseCode = "200") })
    @GetMapping("/available")
    public ResponseEntity<List<BookDTO>> findAvailableBooks() {
        var response = bookService.findAvailableBooks();
        return ResponseEntity.ok(response);
    }
}
