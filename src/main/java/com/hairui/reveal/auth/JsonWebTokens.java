package com.hairui.reveal.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/** A utility class for parsing and generating json web tokens. */
@Component
public final class JsonWebTokens {

  private static final String SECRET_KEY = "337436763979244226452948404D635166546A576E5A7234753778217A25432A";

  /** Parse an input json web token and extra the email address of the user. */
  public static Optional<String> getUserEmail(String token) {
    // TODO: revisit here after the encoding is done to see how we can have a better naming
    return extractClaim(token, Claims::getSubject);
  }

  /** Generate a json web token using the input user details. */
  public static String generateToken(
      UserDetails userDetails
  ) {
    Map<String, Object> extraClaims = new HashMap<>();
    return createJsonWebToken(extraClaims, userDetails);
  }

  private static String createJsonWebToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSigningKey(), SignatureAlgorithm.ES256)
        .compact();
  }

  private static <T> Optional<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
    return extractAllClaims(token).map(claimsResolver::apply);
  }

  private static Optional<Claims> extractAllClaims(String token) {
    return Optional.of(Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
        .parseClaimsJws(token)
        .getBody());
  }

  private static Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
