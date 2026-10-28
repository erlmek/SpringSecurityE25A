package org.example.springsecuritye25a.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProjectSecurityConfig {

    SecurityFilterChain defaultSecurityFilterChainUdenCsrf(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> {
            requests.requestMatchers("/myAccount","/myBalance").authenticated()
                    .requestMatchers("/contact").permitAll();
        });
        http.formLogin(Customizer.withDefaults());
        //http.formLogin(flc -> flc.disable());
        http.httpBasic(Customizer.withDefaults());
        DefaultSecurityFilterChain filterChain = http.build();
        return filterChain;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests((requests) -> { requests
                        .requestMatchers("/myAccount","/myBalance").authenticated()
                        .requestMatchers("/contact", "/register").permitAll();
                });
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        var obj = http.build();
        return obj;
    }



    public PasswordEncoder passwordEncoderTest() {
        //return NoOpPasswordEncoder.getInstance();  //ingen password kryptering
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        // Her sættes styrken til 12 for bcrypt
        encoders.put(idForEncode, new BCryptPasswordEncoder(12));
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        var obj = new DelegatingPasswordEncoder(idForEncode, encoders);
        return obj;

        //var obj = PasswordEncoderFactories.createDelegatingPasswordEncoder(idForEncode, encoders);
        //return obj;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;

        //var obj = PasswordEncoderFactories.createDelegatingPasswordEncoder(idForEncode, encoders);
        //return obj;
    }





}
