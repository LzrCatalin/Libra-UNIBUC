package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.libra.librarymanagement.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
