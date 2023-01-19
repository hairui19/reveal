package com.hairui.reveal.users.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.Set;

@Entity
public class User {

  @Id
  @Column(name = "email")
  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "password")
  private String password;

  @Column(name = "is_enabled")
  private boolean isEnabled;

  @ElementCollection
  @CollectionTable(
      name = "UserRoles",
      joinColumns = @JoinColumn(name = "email")
  )
  @Column(name = "roles")
  private Set<String> roles;

}
