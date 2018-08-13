package com.twigcodes.uaa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Authority extends AbstractAuditingEntity implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(name = "name")
    private String authority;

    @Getter @Setter
    private String code;
    @Getter @Setter
    private String description;

    @Override
    public String getAuthority() {
        return authority;
    }
}
