package com.hairui.reveal.auth;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public final class JwtService {
  public Optional<String> getUserEmail(String jwt) {
    return Optional.empty();
  }
}
