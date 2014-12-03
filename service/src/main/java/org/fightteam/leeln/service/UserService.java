package org.fightteam.leeln.service;

import org.fightteam.leeln.core.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
public interface UserService {

    @PreAuthorize("hasRole('ROLE_USER')")
    User getUser(String username);
}
