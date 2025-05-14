package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    // Users findByUsername(String username);

    boolean existsByUsername(String username);
}
