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
import ro.unibuc.libra.librarymanagement.mapper.AuthorMapper;
import ro.unibuc.libra.librarymanagement.mapper.BookMapper;
import ro.unibuc.libra.librarymanagement.repository.AuthorRepository;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorDTO authorDTO;
    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setBiography("Test biography");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        author.setNationality("American");

        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setFirstName("John");
        authorDTO.setLastName("Doe");
        authorDTO.setBiography("Test biography");
        authorDTO.setBirthDate(LocalDate.of(1980, 1, 1));
        authorDTO.setNationality("American");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
    }

    @Test
    void findById_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(authorRepository).findById(1L);
        verify(authorMapper).toDTO(author);
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authorService.findById(1L));
        verify(authorRepository).findById(1L);
    }

    @Test
    void getAllAuthors_Success() {
        List<Author> authors = List.of(author);
        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        List<AuthorDTO> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstName());
        verify(authorRepository).findAll();
    }

    @Test
    void getAllAuthors_EmptyList() {
        when(authorRepository.findAll()).thenReturn(Collections.emptyList());

        List<AuthorDTO> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(authorRepository).findAll();
    }

    @Test
    void createAuthor_Success() {
        when(authorMapper.toEntity(authorDTO)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.createAuthor(authorDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(authorMapper).toEntity(authorDTO);
        verify(authorRepository).save(author);
        verify(authorMapper).toDTO(author);
    }

    @Test
    void updateAuthor_Success() {
        AuthorDTO updateDTO = new AuthorDTO();
        updateDTO.setFirstName("Jane");
        updateDTO.setLastName("Smith");
        updateDTO.setBiography("Updated biography");
        updateDTO.setBirthDate(LocalDate.of(1985, 5, 15));
        updateDTO.setNationality("British");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(updateDTO);

        AuthorDTO result = authorService.updateAuthor(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        verify(authorRepository).findById(1L);
        verify(authorRepository).save(author);
    }

    @Test
    void updateAuthor_NotFound_ThrowsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authorService.updateAuthor(1L, authorDTO));
        verify(authorRepository).findById(1L);
        verify(authorRepository, never()).save(any());
    }

    @Test
    void deleteAuthor_Success() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);
        doNothing().when(authorRepository).deleteById(1L);

        AuthorDTO result = authorService.deleteAuthor(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(authorRepository).existsById(1L);
        verify(authorRepository).deleteById(1L);
    }

    @Test
    void deleteAuthor_NotFound_ThrowsException() {
        when(authorRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> authorService.deleteAuthor(1L));
        verify(authorRepository).existsById(1L);
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchByName_Success() {
        List<Author> authors = List.of(author);
        when(authorRepository.findByNameContainingIgnoreCase("John")).thenReturn(authors);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        List<AuthorDTO> result = authorService.searchByName("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstName());
        verify(authorRepository).findByNameContainingIgnoreCase("John");
    }

    @Test
    void searchByName_NoResults() {
        when(authorRepository.findByNameContainingIgnoreCase("NonExistent")).thenReturn(Collections.emptyList());

        List<AuthorDTO> result = authorService.searchByName("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(authorRepository).findByNameContainingIgnoreCase("NonExistent");
    }

    @Test
    void findBooksByAuthor_Success() {
        List<Book> books = List.of(book);
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findByAuthorId(1L)).thenReturn(books);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = authorService.findBooksByAuthor(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.getFirst().getTitle());
        verify(authorRepository).existsById(1L);
        verify(bookRepository).findByAuthorId(1L);
    }

    @Test
    void findBooksByAuthor_AuthorNotFound_ThrowsException() {
        when(authorRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> authorService.findBooksByAuthor(1L));
        verify(authorRepository).existsById(1L);
        verify(bookRepository, never()).findByAuthorId(anyLong());
    }

    @Test
    void findBooksByAuthor_NoBooks() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findByAuthorId(1L)).thenReturn(Collections.emptyList());

        List<BookDTO> result = authorService.findBooksByAuthor(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(authorRepository).existsById(1L);
        verify(bookRepository).findByAuthorId(1L);
    }
}
