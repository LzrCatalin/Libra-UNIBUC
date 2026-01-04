package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.mapper.BookMapper;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.service.api.BookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public BookServiceImpl(BookMapper bookMapper,
                           BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDTO findBookById(Long id) {
        return bookMapper.toDTO(
                bookRepository.findByIdWithAuthors(id).orElseThrow(() -> new EntityNotFoundException("Not found"))
        );
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        return bookMapper.toDTO(
                bookRepository.save(
                        bookMapper.toEntity(bookDTO)));
    }
}
