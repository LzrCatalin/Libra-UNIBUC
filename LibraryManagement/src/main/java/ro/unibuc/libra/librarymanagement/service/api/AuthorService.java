package ro.unibuc.libra.librarymanagement.service.api;

import ro.unibuc.libra.librarymanagement.dto.AuthorDTO;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;

import java.util.List;

public interface AuthorService {

    AuthorDTO findById(Long id);
    List<AuthorDTO> getAllAuthors();
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);
    AuthorDTO deleteAuthor(Long id);
    List<AuthorDTO> searchByName(String name);
    List<BookDTO> findBooksByAuthor(Long authorId);
}
