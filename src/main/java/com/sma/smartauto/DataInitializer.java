package com.sma.smartauto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sma.smartauto.repository.ClientRepository;
import com.sma.smartauto.repository.RoleRepository;
import com.sma.smartauto.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    ClientRepository clients;

    @Autowired
    UserRepository users;
    
    @Autowired
    RoleRepository roles;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//        log.debug("initializing vehicles data...");
//        Arrays.asList("moto", "car").forEach(v -> this.clients.saveAndFlush(Client.builder().name(v).build()));
    	try {
//    	Role userRole =  roles.findByName("ROLE_USER");
//    	Role adminRole =  roles.findByName("ROLE_ADMIN");
    	System.out.println("Roles fetched...........");
    	
    	System.out.println(passwordEncoder.encode("3hS.>Jh?"));

//        log.debug("printing all vehicles...");
       this.clients.findAll().forEach(v -> System.out.println(" client :" + v.toString()));
      /* Optional<Client> fcr = clients.findById(1); 
        User user1 = new User();
        user1.setClient(fcr.get());
        user1.setUsername("user1");
        user1.setPassword(this.passwordEncoder.encode("password"));
        user1.setRole(userRole);
        this.users.save(user1);
        
        User user2 = new User();
        Optional<Client> scr = clients.findById(2); 
        user2.setClient(scr.get());
        user2.setUsername("user2");
        user2.setPassword(this.passwordEncoder.encode("password"));
        user2.setRole(adminRole);
        this.users.save(user2);*/
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
//        log.debug("printing all users...");
        this.users.findAll().forEach(v -> System.out.println(" User :" + v.toString()));
    }
}
