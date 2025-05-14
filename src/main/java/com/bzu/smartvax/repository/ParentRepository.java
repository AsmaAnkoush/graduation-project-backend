package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.domain.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByUser(Users user);

    Optional<Parent> findByPhone(String username);

    Optional<Parent> findByUser_Username(String username);
}
