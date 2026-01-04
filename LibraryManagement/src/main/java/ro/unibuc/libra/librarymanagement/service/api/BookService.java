package ro.unibuc.libra.librarymanagement.service.api;

import ro.unibuc.libra.librarymanagement.dto.BookDTO;

public interface BookService {

    BookDTO findBookById(Long id);
    BookDTO createBook(BookDTO bookDTO);
}
