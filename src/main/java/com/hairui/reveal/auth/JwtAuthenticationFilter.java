package com.hairui.reveal.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public final class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static String JWT_HEADER_NAME = "Authorization";
  private static String JWT_PREFIX = "Bearer";

  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final Optional<String> authHeaderOpt = Optional.of(request.getHeader(JWT_HEADER_NAME));
    if (authHeaderOpt.isEmpty() || authHeaderOpt.get().startsWith(JWT_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = authHeaderOpt.get();
    final String jwt = authHeader.substring(JWT_PREFIX.length()).trim();

    // TODO: revisit the logic over here
    Optional<String> usernameOpt = JsonWebTokens.getUserEmail(jwt);
    if (!hasAuthenticated() && usernameOpt.isPresent()) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(usernameOpt.get());
      if (JsonWebTokens.isValidToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }

  private static boolean hasAuthenticated() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).isPresent();
  }
}
