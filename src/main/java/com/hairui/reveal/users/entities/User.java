package com.hairui.reveal.users.entities;

import com.hairui.reveal.users.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Set;

@Entity
public class User {

  @Id
  private String email;

  private String firstName;

  private String lastName;

  private String password;

  private boolean isEnabled;

  private Set<UserRole> roles;

}
