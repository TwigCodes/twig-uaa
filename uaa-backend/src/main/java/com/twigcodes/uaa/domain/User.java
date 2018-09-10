package com.twigcodes.uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.twigcodes.uaa.config.Constants;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(
    callSuper = false,
    of = {"id"})
@ToString(exclude = "authorities")
@Entity
@Table(name = "users")
public class User extends AbstractAuditingEntity implements UserDetails {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Pattern(regexp = Constants.REGEX_LOGIN)
  @Size(min = 1, max = 50)
  @Column(unique = true)
  private String username;

  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 60)
  private String password;

  @NotNull
  @Pattern(regexp = Constants.REGEX_MOBILE)
  @Size(min = 10, max = 15)
  @Column(unique = true)
  private String mobile;

  @Size(max = 50)
  private String name;

  @JsonIgnore
  @Column(name = "pinyin_name_initials")
  private String pinyinNameInitials;

  @Email
  @Size(min = 5, max = 254)
  @Column(unique = true)
  private String email;

  @Size(max = 256)
  private String avatar;

  private boolean enabled;
  private boolean accountNonExpired;
  private boolean credentialsNonExpired;
  private boolean accountNonLocked;

  @JsonIgnore
  @Singular("authority")
  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_roles_relation",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Authority> authorities;
}
