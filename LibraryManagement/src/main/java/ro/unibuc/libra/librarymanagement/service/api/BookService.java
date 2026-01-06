package ro.unibuc.libra.librarymanagement.service.api;

import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.util.enums.BookCategory;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);
    List<BookDTO> getAllBooks();
    BookDTO updateBook(Long id, BookDTO bookDTO);
    BookDTO deleteBook(Long id);

    List<BookDTO> searchByTitle(String title);

    List<BookDTO> findByCategory(BookCategory category);
    List<BookDTO> findAvailableBooks();
    BookDTO findBookById(Long id);
}
