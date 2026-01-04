package ro.unibuc.libra.librarymanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.libra.librarymanagement.dto.AuthorDTO;
import ro.unibuc.libra.librarymanagement.dto.BookDTO;
import ro.unibuc.libra.librarymanagement.entity.Author;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.entity.BookAuthor;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book, Long> {

    @Override
    @Mapping(target = "authors", source = "bookAuthors")
    BookDTO toDTO(Book entity);

    default List<AuthorDTO> mapBookAuthorsToAuthors(List<BookAuthor> bookAuthors) {
        if (bookAuthors == null || bookAuthors.isEmpty()) {
            return null;
        }
        return bookAuthors.stream()
                .map(BookAuthor::getAuthor)
                .filter(author -> author != null)
                .map(this::authorToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Mapare Author → AuthorDTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "biography", source = "biography")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "nationality", source = "nationality")
    AuthorDTO authorToDTO(Author author);
}
