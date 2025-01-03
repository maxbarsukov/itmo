package server.services.auth;

import com.google.common.hash.Hashing;
import jakarta.ejb.*;

import jakarta.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import server.configuration.Configurable;
import server.models.User;
import server.daos.UserDAO;
import server.rest.dtos.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Stateless
public class AuthService {
  @Inject
  @Configurable("authentication.auth.saltLength")
  private Integer saltLength;

  @Inject
  @Configurable("authentication.auth.pepper")
  private String pepper;

  @EJB
  private UserDAO users;
  @EJB
  private TokenService tokenService;

  public Optional<User> getUserById(Integer id) {
    return users.findById(id);
  }

  /**
   * Registers and returns token if everything is fine.
   *
   * @param username username to register
   * @param password password to register
   * @return AuthResponse with token if correct / AuthResponse with errorMessage if not
   */
  public AuthResponse register(String username, String password) {
    final Optional<User> optionalUser = users.findByName(username);
    if (optionalUser.isPresent()) {
      return AuthResponse.message("USER_ALREADY_EXISTS");
    }

    var salt = generateSalt();
    var passwordHash = generatePasswordHash(password, salt);

    var user = new User(username, passwordHash, salt);
    users.save(user);

    return AuthResponse.registered(tokenService.generate(String.valueOf(user.getId())), user);
  }

  /**
   * Checks if username exists, checks password and returns token if everything is fine.
   *
   * @param username username to check
   * @param password password to check
   * @return AuthResponse with token if correct / AuthResponse with errorMessage if not
   */
  public AuthResponse login(String username, String password) {
    final Optional<User> optionalUser = users.findByName(username);
    if (optionalUser.isEmpty()) {
      return AuthResponse.message("USER_NOT_FOUND");
    }

    var user = optionalUser.get();
    if (user.getPasswordDigest().equals(generatePasswordHash(password, user.getSalt()))) {
      return AuthResponse.token(tokenService.generate(String.valueOf(user.getId())), user);
    }

    return AuthResponse.message("WRONG_PASSWORD");
  }

  /**
   * Accepts token and decodes username from it.
   * Also verifies the token so the action by user who sent this token is authorized.
   *
   * @param token token to decode.
   * @return username from token if the token is valid.
   */
  public Optional<String> getUserIdByToken(String token) {
    return tokenService.verify(token);
  }

  private String generateSalt() {
    return RandomStringUtils.randomAlphanumeric(saltLength);
  }

  private String generatePasswordHash(String password, String salt) {
    return Hashing.sha256()
      .hashString(pepper + password + salt, StandardCharsets.UTF_8)
      .toString();
  }
}
