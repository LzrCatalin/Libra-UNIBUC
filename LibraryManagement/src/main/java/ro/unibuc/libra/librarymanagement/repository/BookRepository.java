package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.libra.librarymanagement.entity.Book;
import ro.unibuc.libra.librarymanagement.util.enums.BookCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b " +
            "LEFT JOIN FETCH b.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE b.id = :id")
    Optional<Book> findByIdWithAuthors(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author")
    List<Book> findAllWithAuthors();

    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE b.bookCategory = :category")
    List<Book> findByBookCategory(@Param("category") BookCategory category);

    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE b.availableCopies > 0")
    List<Book> findAvailableBooks();

    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author a " +
            "WHERE a.id = :authorId")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);
}
