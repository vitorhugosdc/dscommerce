package com.vitor.dscommerce.services;

import com.vitor.dscommerce.entities.Role;
import com.vitor.dscommerce.entities.User;
import com.vitor.dscommerce.projections.UserDetailsProjection;
import com.vitor.dscommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/*Check-list Spring Security 3, UserDetailsService em UserService*/
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    /*UsernameNotFoundException é uma exceção do próprio Spring Security, então usamos ela*/
     /* loadUserByUsername é um método da interface UserDetailsService*/
    /*Carrega do banco de dados um usuário pelo seu username (que é o email com @Column(unique = true))
     * Estamos utilizando projection para carregar o usuário com suas roles já em uma única consulta*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*List pois o usuário pode ter mais de 1 role*/
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = new User();
        /*Poderia ser result.get(0).getUsername() pois a projection do getUsername() não está sendo usada, tanto faz*/
        user.setEmail(username);
        /*Independente do número de resultados, a senha em cada linha vai ser a mesma do mesmo usuário, então pegamos a primeira senha*/
        user.setPassword(result.get(0).getPassword());
        /*Aqui é a única coluna que pode mudar, as roles, que pode ser mais de uma*/
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getRoleAuthority()));
        }
        return user;
    }
}
