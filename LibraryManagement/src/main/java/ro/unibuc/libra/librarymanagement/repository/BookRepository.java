package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ro.unibuc.libra.librarymanagement.entity.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b " +
            "LEFT JOIN FETCH b.bookAuthors ba " +
            "LEFT JOIN FETCH ba.author " +
            "WHERE b.id = :id")
    Optional<Book> findByIdWithAuthors(@PathVariable Long id);
}
