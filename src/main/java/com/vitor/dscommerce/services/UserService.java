package com.vitor.dscommerce.services;

import com.vitor.dscommerce.dto.UserDTO;
import com.vitor.dscommerce.entities.Role;
import com.vitor.dscommerce.entities.User;
import com.vitor.dscommerce.projections.UserDetailsProjection;
import com.vitor.dscommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /*Retorna o usuário que está logado*/
    @Transactional(readOnly = true)
    protected User authenticated() {

        try {
            /*Pega um objeto Authentication no contexto do Spring Security, ou seja, se tiver um usuário autenticado ele vai buscar o objeto para mim*/

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            /* Dentro desse objeto como estamos usando o padrão Jwt, conseguimos chamar um getPrincipal fazendo casting para o tipo Jwt*/
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            /*Esse claim de username (que é o email) tá definido lá no authorization server*/
            String username = jwtPrincipal.getClaim("username");
            return repository.findByEmail(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User user = authenticated();
        return new UserDTO(user);
    }

}
