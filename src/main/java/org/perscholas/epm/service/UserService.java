package org.perscholas.epm.service;

import jakarta.transaction.Transactional;
import org.perscholas.epm.dao.RoleRepo;
import org.perscholas.epm.dao.UserRepo;

import org.perscholas.epm.model.Role;
import org.perscholas.epm.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private UserRepo userRepo;

    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userDao,
                       RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userDao;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User loadUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User createUser(String email, String password) {
        User user = loadUserByEmail(email);
        if (user != null) throw new RuntimeException("email: " + email + "already exists");
        String encodedPassword = passwordEncoder.encode(password);
        return userRepo.save(new User(email, encodedPassword));
    }

    public void assignRoleToUser(String email, String roleName) {
        User user = loadUserByEmail(email);
        Role role = roleRepo.findByName(roleName);
        user.assignRoleToUser(role);

    }

    public boolean doesCurrentUserHasRole(String roleName) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }
}
