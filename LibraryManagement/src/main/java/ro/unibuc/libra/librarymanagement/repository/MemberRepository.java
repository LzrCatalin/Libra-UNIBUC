package ro.unibuc.libra.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.libra.librarymanagement.entity.Member;
import ro.unibuc.libra.librarymanagement.util.enums.MemberStatus;
import ro.unibuc.libra.librarymanagement.util.enums.MembershipType;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m " +
            "WHERE LOWER(m.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Member> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT m FROM Member m " +
            "WHERE LOWER(m.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Member> findByEmailContainingIgnoreCase(@Param("email") String email);

    List<Member> findByMembershipType(MembershipType membershipType);

    List<Member> findByStatus(MemberStatus status);
}
