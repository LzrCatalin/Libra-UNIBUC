package ro.unibuc.libra.librarymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.AuthorDTO;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.service.api.AuthorService;

import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.AUTHORS;

@RestController
@RequestMapping(AUTHORS)
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        var response = authorService.getAllAuthors();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> fetchAuthor(@PathVariable("id") Long id) {
        var response = authorService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO) {
        var response = authorService.createAuthor(authorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDTO authorDTO) {
        var response = authorService.updateAuthor(id, authorDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDTO> deleteAuthor(@PathVariable("id") Long id) {
        var response = authorService.deleteAuthor(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AuthorDTO>> searchByName(@RequestParam("name") String name) {
        var response = authorService.searchByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO>> findBooksByAuthor(@PathVariable("id") Long id) {
        var response = authorService.findBooksByAuthor(id);
        return ResponseEntity.ok(response);
    }
}
