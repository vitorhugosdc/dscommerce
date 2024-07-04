package com.vitor.dscommerce.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_role")
/*Check-list Spring Security 1, GrantedAuthority em Roles*/
/*A interface GrantedAuthority requer que implementação do getAuthority(), mas como estamos usando o nome do atributo já como authority, já existe esse get*/
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*authority porque é o nome que o Spring Security utiliza, mas pode ser qualquer um, ai teria que ter o get retornando esse outro nome*/
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();


    public Role() {
    }

    public Role(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    /*@Override pra indicar pro compilador que é a implementação do método da interface GrantedAuthority mesmo*/
    @Override
    public String getAuthority() {
        return authority;
    }

    /*Caso especial onde não é o id comparado e sim o nome, pois uma ROLE não vai ter o mesmo nome, é único*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(authority, role.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authority);
    }
}
