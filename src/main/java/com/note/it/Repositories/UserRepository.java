package com.note.it.Repositories;


import com.note.it.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    int countUserByEmailId(String emailId);

    Optional<User> findByEmailId(String emailId);
}
