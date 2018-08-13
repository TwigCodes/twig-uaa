package com.twigcodes.uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.twigcodes.uaa.config.Constants;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"id"})
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
    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles_relation",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Authority> authorities;
}
