package com.hairui.reveal.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static String JWT_HEADER_NAME = "Authorization";
  private static String JWT_PREFIX = "Bearer";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final Optional<String> authHeaderOpt = Optional.of(request.getHeader(JWT_HEADER_NAME));
    if (authHeaderOpt.isEmpty() || authHeaderOpt.get().startsWith(JWT_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }
    // Do some kind of authentication over here.
  }
}
