package com.vitor.dscommerce.services;

import com.vitor.dscommerce.entities.User;
import com.vitor.dscommerce.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public void validadeSelfOrAdmin(Long userId) {
        User me = userService.authenticated();
        /*verifica se o usuário logado é ADMIN ou se tem o mesmo id recebido, esse id recebido é id do cliente armazenado no pedido*/
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Access denied");
        }
    }
}
