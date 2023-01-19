package com.hairui.reveal.users.repository;

import com.hairui.reveal.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
  UserDetails findUserByEmail(String email);
}
