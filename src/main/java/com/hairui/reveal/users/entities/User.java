package com.hairui.reveal.users.entities;

import com.hairui.reveal.users.UserRole;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "Users")
public class User implements UserDetails {

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

  public User(String email, String firstName, String lastName, String password, boolean isEnabled, Set<UserRole> userRoles) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.isEnabled = isEnabled;
    this.roles = userRoles.stream().map(UserRole::name).collect(Collectors.toSet());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    // Not using right now.
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    // Not using right now.
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // Not using right now.
    return false;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  private User() {}
}
