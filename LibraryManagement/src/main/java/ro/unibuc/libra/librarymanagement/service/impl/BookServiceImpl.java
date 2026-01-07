package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ro.unibuc.libra.librarymanagement.dto.AuthorDTO;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.entity.Author;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.entity.BookAuthor;
import ro.unibuc.libra.librarymanagement.mapper.BookMapper;
import ro.unibuc.libra.librarymanagement.repository.AuthorRepository;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.service.api.BookService;
import ro.unibuc.libra.librarymanagement.util.enums.BookCategory;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookMapper bookMapper,
                           BookRepository bookRepository,
                           AuthorRepository authorRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public BookDTO findBookById(Long id) {
        return bookMapper.toDTO(
                bookRepository.findByIdWithAuthors(id).orElseThrow(() -> new EntityNotFoundException("Not found"))
        );
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        List<Author> authors = validateAndFetchAuthors(bookDTO.getAuthors());
        Book book = bookMapper.toEntity(bookDTO);

        for (Author author : authors) {
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBook(book);
            bookAuthor.setAuthor(author);
            book.getBookAuthors().add(bookAuthor);
        }

        return bookMapper.toDTO(bookRepository.save(book));
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
        List<Author> authors = validateAndFetchAuthors(bookDTO.getAuthors());

        Book existingBook = bookRepository.findByIdWithAuthors(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        existingBook.setAvailableCopies(bookDTO.getAvailableCopies());
        existingBook.setBookCategory(bookDTO.getBookCategory());

        existingBook.getBookAuthors().clear();

        for (Author author : authors) {
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBook(existingBook);
            bookAuthor.setAuthor(author);
            existingBook.getBookAuthors().add(bookAuthor);
        }

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

    private List<Author> validateAndFetchAuthors(List<AuthorDTO> authorDTOs) {
        if (authorDTOs == null || authorDTOs.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one author");
        }

        return authorDTOs.stream()
                .map(authorDTO -> authorRepository.findById(authorDTO.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorDTO.getId())))
                .toList();
    }
}
