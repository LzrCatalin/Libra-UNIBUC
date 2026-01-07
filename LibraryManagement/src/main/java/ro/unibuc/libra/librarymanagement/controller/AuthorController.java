package ro.unibuc.libra.librarymanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.libra.librarymanagement.dto.AuthorDTO;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.service.api.AuthorService;

import java.util.List;

import static ro.unibuc.libra.librarymanagement.util.ApiConstants.AUTHORS;

@Tag(
        name = "Authors",
        description = "Operations related to authors (CRUD, search, and author books)"
)
@RestController
@RequestMapping(AUTHORS)
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(
            summary = "Get all authors",
            description = "Returns the full list of authors."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        var response = authorService.getAllAuthors();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get author by id",
            description = "Fetches a single author by its identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> fetchAuthor(
            @Parameter(description = "Author id", example = "1", required = true)
            @PathVariable("id") Long id
    ) {
        var response = authorService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create author",
            description = "Creates a new author and returns the created resource."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payload", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Author data to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthorDTO.class))
            )
            @RequestBody AuthorDTO authorDTO
    ) {
        var response = authorService.createAuthor(authorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update author",
            description = "Updates an existing author by id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payload", content = @Content),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(
            @Parameter(description = "Author id", example = "1", required = true)
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated author data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthorDTO.class))
            )
            @RequestBody AuthorDTO authorDTO
    ) {
        var response = authorService.updateAuthor(id, authorDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete author",
            description = "Deletes an author by id and returns the deleted author."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDTO> deleteAuthor(
            @Parameter(description = "Author id", example = "1", required = true)
            @PathVariable("id") Long id
    ) {
        var response = authorService.deleteAuthor(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Search authors by name",
            description = "Searches authors using a (partial) name match."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search executed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid query parameter", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<AuthorDTO>> searchByName(
            @Parameter(description = "Name (partial match) to search for", example = "Rowling", required = true)
            @RequestParam("name") String name
    ) {
        var response = authorService.searchByName(name);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get books by author",
            description = "Returns all books written by the author identified by id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
    })
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO>> findBooksByAuthor(
            @Parameter(description = "Author id", example = "1", required = true)
            @PathVariable("id") Long id
    ) {
        var response = authorService.findBooksByAuthor(id);
        return ResponseEntity.ok(response);
    }
}
