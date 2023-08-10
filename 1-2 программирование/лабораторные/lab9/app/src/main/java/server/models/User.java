package server.models;

import common.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="users")
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true)
  private int id;

  @Column(name="name", length=40, unique=true, nullable=false)
  private String name;

  @Column(name="password_digest", length=64, nullable=false)
  private String passwordDigest;

  @Column(name="salt", length=10, nullable=false)
  private String salt;

  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinColumn(name="creator_id")
  @ToString.Exclude
  private List<Organization> organizations;

  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinColumn(name="creator_id")
  @ToString.Exclude
  private List<Product> products;;

  public User(String name, String passwordDigest, String salt) {
    this.name = name;
    this.passwordDigest = passwordDigest;
    this.salt = salt;
  }

  public UserDTO toDTO() {
    return new UserDTO(this.id, this.name);
  }
}
