package com.bzu.smartvax.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String PARENT = "ROLE_PARENT";
    public static final String HEALTH_WORKER = "ROLE_HEALTH_WORKER";


    private AuthoritiesConstants() {}
}
