package pt.psoft.g1.psoftg1.usermanagement.services;

import pt.psoft.g1.psoftg1.usermanagement.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.psoft.g1.psoftg1.usermanagement.model.MongoUser;


public class UserConverter {

    @Autowired
    private static PasswordEncoder passwordEncoder;

    public static MongoUser toMongoUser(User user) {
        MongoUser mongoUser = new MongoUser(user.getUsername(), user.getPassword());
        mongoUser.setName(user.getName());
        mongoUser.setEnabled(user.isEnabled());
        mongoUser.getAuthorities().addAll(user.getAuthorities());
        return mongoUser;
    }

    public static User toUser(MongoUser mongoUser) {
        User user = new User(mongoUser.getUsername(), mongoUser.getPassword());
        user.setName(mongoUser.getName().getName());
        user.setEnabled(mongoUser.isEnabled());
        mongoUser.getAuthorities().forEach(user::addAuthority);
        return user;
    }
}
