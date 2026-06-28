package org.example.springeshopapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.example.springeshopapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
        Optional<User> findByEmail(String email);
}
