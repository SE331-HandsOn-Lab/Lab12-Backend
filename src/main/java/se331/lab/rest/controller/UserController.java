package se331.lab.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se331.lab.rest.entity.Organizer;
import se331.lab.rest.repository.OrganizerRepository;
import se331.lab.rest.security.entity.Authority;
import se331.lab.rest.security.entity.User;
import se331.lab.rest.security.repository.AuthorityRepository;
import se331.lab.rest.security.repository.UserRepository;
import se331.lab.rest.util.LabMapper;


@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizerRepository organizerRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        Authority authUser = authorityRepository.getById(1L);
        Organizer organizer = organizerRepository.save(Organizer.builder().name(user.getUsername()).id(Long.parseLong(String.valueOf(organizerRepository.findAll().size() + 1))).build());
        organizer.setUser(user);
        String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        user.setEnabled(true);
        user.getAuthorities().add(authUser);
        user.setOrganizer(organizer);
        User u = userRepository.save(user);
        return ResponseEntity.ok(LabMapper.INSTANCE.getUserDTO(u));
    }
}
