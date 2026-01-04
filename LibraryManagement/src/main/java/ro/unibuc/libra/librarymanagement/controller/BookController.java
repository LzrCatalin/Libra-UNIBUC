package ro.unibuc.libra.librarymanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.service.api.BookService;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.BOOKS;

@RestController
@RequestMapping(BOOKS)
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> fetchBook(@PathVariable("id") Long id) {
        var response = bookService.findBookById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        var response = bookService.createBook(bookDTO);
        return ResponseEntity.ok(response);
    }
}
