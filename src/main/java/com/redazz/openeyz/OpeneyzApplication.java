package com.redazz.openeyz;

import com.redazz.openeyz.beans.Encryptor;
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
    @Autowired
    UserService us;
    @Autowired
    RoleService rs;
    @Autowired Encryptor encryptor;

    public static void main(String[] args) {
        SpringApplication.run(OpeneyzApplication.class, args);
    }
    @Bean
    public CommandLineRunner initCfg() {
        return (String[] args) -> {
            Users suadmin = new Users("suadmin@gmail.com",encryptor.encrypt("SuperAdmin".getBytes("utf-8")), "SuperAdmin", "1234abdcL?", "Je suis super administrateur");
            Users admin = new Users("admin@gmail.com", encryptor.encrypt("Admin".getBytes("utf-8")), "Admin", "1234abdcL?", "Je suis administrateur");
            Users user = new Users("user@gmail.com", encryptor.encrypt("User".getBytes("utf-8")), "User", "1234abdcL?", "je suis utilisateur");
            
            us.save(suadmin);
            us.save(admin);
            us.save(user);

            Role suadminRole = new Role(RoleEnum.SUPERADMIN);
            Role adminRole = new Role(RoleEnum.ADMIN);
            Role userRole = new Role(RoleEnum.USER);
            rs.save(suadminRole);
            rs.save(adminRole);
            rs.save(userRole);

            us.addRoleToUser(suadmin.getUsername(), suadminRole.getRoleName());
            us.addRoleToUser(admin.getUsername(), adminRole.getRoleName());
            us.addRoleToUser(user.getUsername(), userRole.getRoleName());            
        };
    }
}
