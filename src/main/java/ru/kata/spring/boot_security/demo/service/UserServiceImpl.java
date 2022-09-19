package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service class for {@link User}
 */
@Service
public class UserServiceImpl {
    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userDao.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userDao.findAll();
    }

    public boolean saveUser(User user) {
        System.out.println("Start to save user...");
        User userFromDB = userDao.findByUsername(user.getUsername());
        System.out.println("User from DB: " + userFromDB);
        System.out.println("User from register form: " + user);

        if (userFromDB != null) {
            return false;
        }
        roleDao.save(new Role(1L, "ROLE_USER"));

        user.setRoles(Collections.singleton(roleDao.getById(1L)));
        userDao.save(user);
        System.out.println("User was save: " + user);
        return true;
    }

//    public void update(long id, User user) {
//        User userFromDB = userDao.findById(id).orElse(new User());
//        userFromDB.setUsername(user.getUsername());
//        userFromDB.setPassword(user.getPassword());
//        userFromDB.setFullname(user.getFullname());
//        userFromDB.setEmail(user.getEmail());
//        userFromDB.setPhone(user.getPhone());
//        userDao.save(userFromDB);
//    }

//    public boolean deleteUser(Long userId) {
//        if (userDao.findById(userId).isPresent()) {
//            userDao.deleteById(userId);
//            return true;
//        }
//        return false;
//    }
}
