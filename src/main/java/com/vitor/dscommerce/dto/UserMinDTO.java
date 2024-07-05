package com.vitor.dscommerce.dto;

import com.vitor.dscommerce.entities.User;
import com.vitor.dscommerce.projections.UserMinProjection;

public class UserMinDTO {

    private Long id;
    private String name;

    public UserMinDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserMinDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
    }

    public UserMinDTO(UserMinProjection projection) {
        id = projection.getId();
        name = projection.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
