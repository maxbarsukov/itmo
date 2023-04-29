package server.services.auth;

import io.jsonwebtoken.*;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import server.configuration.Configurable;
import server.utils.KeywordKeyGenerator;

import java.util.Optional;

@Stateless
public class TokenService {
  @Inject
  @Configurable("authentication.jwt.secret")
  private String secret;

  /**
   * @param userId user ID to generate token for
   * @return Generated token
   */
  public String generate(String userId) {
    var key = new KeywordKeyGenerator(secret).generate();
    return Jwts.builder()
      .setSubject(userId)
      .signWith(key, SignatureAlgorithm.HS512)
      .compact();
  }

  /**
   * @param token Token to verify and extract user ID
   * @return An optional with the user ID if verified successfully
   */
  public Optional<String> verify(String token) {
    try {
      var key = new KeywordKeyGenerator(secret).generate();

      Jws<Claims> claimsJws = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);

      return Optional.of(claimsJws.getBody().getSubject());
    } catch (JwtException e) {
      System.err.println("Bad JWT token: " + e.getLocalizedMessage());
      return Optional.empty();
    }
  }
}
