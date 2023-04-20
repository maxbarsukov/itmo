package server.services.auth;

import io.jsonwebtoken.*;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import server.utils.KeywordKeyGenerator;

import java.security.Key;
import java.util.Optional;

@Stateless
public class TokenService {
  @Inject
  private Logger logger;

  private final Key key = new KeywordKeyGenerator("MY_SUPER_KEY").generate();

  /**
   * @param userId user ID to generate token for
   * @return Generated token
   */
  public String generate(String userId) {
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
      Jws<Claims> claimsJws = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);

      return Optional.of(claimsJws.getBody().getSubject());
    } catch (JwtException e) {
      logger.error("Bad JWT token", e);
      return Optional.empty();
    }
  }
}
