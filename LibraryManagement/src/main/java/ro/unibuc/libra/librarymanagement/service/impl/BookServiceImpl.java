package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.mapper.BookMapper;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.service.api.BookService;
import ro.unibuc.libra.librarymanagement.util.enums.BookCategory;

import java.util.List;

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

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAllWithAuthors()
                .stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findByIdWithAuthors(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        existingBook.setAvailableCopies(bookDTO.getAvailableCopies());
        existingBook.setBookCategory(bookDTO.getBookCategory());

        return bookMapper.toDTO(bookRepository.save(existingBook));
    }

    @Override
    public BookDTO deleteBook(Long id) {
        if (!bookRepository.existsById(id))
            throw new EntityNotFoundException("Book not found with id: " + id);

        BookDTO bookDTO = bookMapper.toDTO(bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found")));
        bookRepository.deleteById(id);

        return bookDTO;
    }

    @Override
    public List<BookDTO> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @Override
    public List<BookDTO> findByCategory(BookCategory category) {
        return bookRepository.findByBookCategory(category)
                .stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @Override
    public List<BookDTO> findAvailableBooks() {
        return bookRepository.findAvailableBooks()
                .stream()
                .map(bookMapper::toDTO)
                .toList();
    }
}
