package com.example.ScienceCentre.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ScienceCentre.Model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndActiveTrue(String username);
    boolean existsByRole(String role);
}
