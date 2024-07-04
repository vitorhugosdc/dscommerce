package com.vitor.dscommerce.projections;

public interface UserDetailsProjection {

    String getUsername();
    String getPassword();
    Long getRoleId();
    String getRoleAuthority();
}
