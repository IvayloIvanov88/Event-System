package org.softuni.eventures.service;

import org.softuni.eventures.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.transaction.NotSupportedException;
import java.util.Set;

public interface UserService extends UserDetailsService {
    boolean createUser(UserServiceModel userServiceModel);

    Set<UserServiceModel> getAll();

    UserServiceModel getById(String id);

    UserServiceModel getByUsername(String username);

    boolean promoteUser(String id);

    boolean demoteUser(String id);
}
