package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.libra.librarymanagement.entity.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT DISTINCT a FROM Author a " +
            "LEFT JOIN FETCH a.bookAuthors ba " +
            "LEFT JOIN FETCH ba.book " +
            "WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Author> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT a FROM Author a " +
            "LEFT JOIN FETCH a.bookAuthors")
    List<Author> findAllWithBooks();
}
