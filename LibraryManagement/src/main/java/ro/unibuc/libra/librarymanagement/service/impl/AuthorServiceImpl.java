package ro.unibuc.libra.librarymanagement.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ro.unibuc.libra.librarymanagement.dto.AuthorDTO;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.entity.Author;
import ro.unibuc.libra.librarymanagement.mapper.AuthorMapper;
import ro.unibuc.libra.librarymanagement.mapper.BookMapper;
import ro.unibuc.libra.librarymanagement.repository.AuthorRepository;
import ro.unibuc.libra.librarymanagement.repository.BookRepository;
import ro.unibuc.libra.librarymanagement.service.api.AuthorService;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public AuthorServiceImpl(AuthorMapper authorMapper,
                             AuthorRepository authorRepository,
                             BookMapper bookMapper,
                             BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }

    @Override
    public AuthorDTO findById(Long id) {
        return authorMapper.toDTO(
                authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id))
        );
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDTO)
                .toList();
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        return authorMapper.toDTO(
                authorRepository.save(
                        authorMapper.toEntity(authorDTO)));
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));

        existingAuthor.setFirstName(authorDTO.getFirstName());
        existingAuthor.setLastName(authorDTO.getLastName());
        existingAuthor.setBiography(authorDTO.getBiography());
        existingAuthor.setBirthDate(authorDTO.getBirthDate());
        existingAuthor.setNationality(authorDTO.getNationality());

        return authorMapper.toDTO(authorRepository.save(existingAuthor));
    }

    @Override
    public AuthorDTO deleteAuthor(Long id) {
        if (!authorRepository.existsById(id))
            throw new EntityNotFoundException("Author not found with id: " + id);

        AuthorDTO authorDTO = authorMapper.toDTO(authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id)));
        authorRepository.deleteById(id);

        return authorDTO;
    }

    @Override
    public List<AuthorDTO> searchByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(authorMapper::toDTO)
                .toList();
    }

    @Override
    public List<BookDTO> findBooksByAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId))
            throw new EntityNotFoundException("Author not found with id: " + authorId);

        return bookRepository.findByAuthorId(authorId)
                .stream()
                .map(bookMapper::toDTO)
                .toList();
    }
}
