package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.libra.librarymanagement.dto.AuthorDTO;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.entity.Author;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.entity.BookAuthor;
import ro.unibuc.libra.librarymanagement.mapper.BookMapper;
import ro.unibuc.libra.librarymanagement.repository.AuthorRepository;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.util.enums.BookCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDTO bookDTO;
    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");

        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setFirstName("John");
        authorDTO.setLastName("Doe");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setPublicationYear(2024);
        book.setAvailableCopies(5);
        book.setBookCategory(BookCategory.FICTION);
        book.setBookAuthors(new ArrayList<>());

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setDescription("Test Description");
        bookDTO.setPublicationYear(2024);
        bookDTO.setAvailableCopies(5);
        bookDTO.setBookCategory(BookCategory.FICTION);
        bookDTO.setAuthors(List.of(authorDTO));
    }

    @Test
    void createBook_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookMapper.toEntity(bookDTO)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.createBook(bookDTO);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(authorRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toDTO(book);
    }

    @Test
    void createBook_WithEmptyAuthors_ThrowsException() {
        bookDTO.setAuthors(Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(bookDTO));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_WithNullAuthors_ThrowsException() {
        bookDTO.setAuthors(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(bookDTO));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_WithNonExistentAuthor_ThrowsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.createBook(bookDTO));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void findBookById_Success() {
        when(bookRepository.findByIdWithAuthors(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.findBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository).findByIdWithAuthors(1L);
    }

    @Test
    void findBookById_NotFound_ThrowsException() {
        when(bookRepository.findByIdWithAuthors(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findBookById(1L));
    }

    @Test
    void getAllBooks_Success() {
        List<Book> books = List.of(book);
        List<BookDTO> bookDTOs = List.of(bookDTO);
        when(bookRepository.findAllWithAuthors()).thenReturn(books);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.getFirst().getTitle());
        verify(bookRepository).findAllWithAuthors();
    }

    @Test
    void getAllBooks_EmptyList() {
        when(bookRepository.findAllWithAuthors()).thenReturn(Collections.emptyList());

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository).findAllWithAuthors();
    }

    @Test
    void updateBook_Success() {
        BookDTO updateDTO = new BookDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setPublicationYear(2025);
        updateDTO.setAvailableCopies(10);
        updateDTO.setBookCategory(BookCategory.NON_FICTION);
        updateDTO.setAuthors(List.of(authorDTO));

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findByIdWithAuthors(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(updateDTO);

        BookDTO result = bookService.updateBook(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(bookRepository).findByIdWithAuthors(1L);
        verify(bookRepository).save(book);
    }

    @Test
    void updateBook_WithEmptyAuthors_ThrowsException() {
        bookDTO.setAuthors(Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, bookDTO));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_BookNotFound_ThrowsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findByIdWithAuthors(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(1L, bookDTO));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_AuthorNotFound_ThrowsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(1L, bookDTO));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);
        doNothing().when(bookRepository).deleteById(1L);

        BookDTO result = bookService.deleteBook(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_NotFound_ThrowsException() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(1L));
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchByTitle_Success() {
        List<Book> books = List.of(book);
        when(bookRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(books);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.searchByTitle("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.getFirst().getTitle());
        verify(bookRepository).findByTitleContainingIgnoreCase("Test");
    }

    @Test
    void findByCategory_Success() {
        List<Book> books = List.of(book);
        when(bookRepository.findByBookCategory(BookCategory.FICTION)).thenReturn(books);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.findByCategory(BookCategory.FICTION);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BookCategory.FICTION, result.getFirst().getBookCategory());
        verify(bookRepository).findByBookCategory(BookCategory.FICTION);
    }

    @Test
    void findAvailableBooks_Success() {
        List<Book> books = List.of(book);
        when(bookRepository.findAvailableBooks()).thenReturn(books);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.findAvailableBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.getFirst().getAvailableCopies() > 0);
        verify(bookRepository).findAvailableBooks();
    }
}
