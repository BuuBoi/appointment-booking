package com.dut.doctorcare.config;

import com.dut.doctorcare.dao.iface.RoleDao;
import com.dut.doctorcare.dao.iface.UserDao;
import com.dut.doctorcare.model.Role;
import com.dut.doctorcare.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ApplicationStartup {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createAdminUser() {
        if (userDao.findByEmail("admin@gmail.com").isEmpty()) {
            if(roleDao.findRole("ADMIN") == null){
                Role adminRole = new Role();
                adminRole.setRoleName("ADMIN");
                roleDao.save(adminRole);
            }
            log.error("Admin role created");
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPasswordHash(passwordEncoder.encode("admin123"));
            //adminUser.setDisplayName("Admin");
            adminUser.setRole(roleDao.findRole("ADMIN"));
            userDao.save(adminUser);
            System.out.println("Admin user created");
        }
    }
}

