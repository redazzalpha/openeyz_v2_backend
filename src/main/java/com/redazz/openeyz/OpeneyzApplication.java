package com.redazz.openeyz;

import com.redazz.openeyz.enums.RoleEnum;
import com.redazz.openeyz.models.Role;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.RoleService;
import com.redazz.openeyz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpeneyzApplication {
    @Autowired UserService us;
    @Autowired RoleService rs;

    public static void main(String[] args) {
        SpringApplication.run(OpeneyzApplication.class, args);
    }
    @Bean
    public CommandLineRunner initCfg() {
        return args -> {
            Users max = new Users("max@gmail.com", "Dubois", "Max", "1234abdcL?", "");
            Users alice = new Users("alice@gmail.com", "Dubois", "alice", "4321LpTZ28!", "");
            us.save(max);
            us.save(alice);

            Role user = new Role(RoleEnum.USER);
            Role admin = new Role(RoleEnum.ADMIN);
            Role superadmin = new Role(RoleEnum.SUPERADMIN);
            rs.save(user);
            rs.save(admin);
            rs.save(superadmin);

            // TODO: remove possibility to add more than one role by creation one to one relation between users and role
            us.addRoleToUser(max.getUsername(), superadmin.getRoleName());
            us.addRoleToUser(alice.getUsername(), admin.getRoleName());
        };
    }
}
