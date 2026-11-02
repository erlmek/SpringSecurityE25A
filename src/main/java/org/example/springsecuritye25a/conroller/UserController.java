package org.example.springsecuritye25a.conroller;

import org.example.springsecuritye25a.model.Customer;
import org.example.springsecuritye25a.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/auth")
    public Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    @PostMapping("/dologin")
    public ResponseEntity<String> doLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Du er logget på");
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer savedCustomer = null;
        ResponseEntity response = null;
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() > 0) {
                response = ResponseEntity.status(HttpStatus.CREATED)
                        .body("Given user details are successfully registrered");
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to" + ex.getMessage());
        }
        return response;
    }
}



