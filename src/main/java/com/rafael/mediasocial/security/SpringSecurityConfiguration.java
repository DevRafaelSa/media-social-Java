package com.rafael.mediasocial.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //indica que essa classe declara bean metodos
public class SpringSecurityConfiguration {

     //com esse metodo eu sobrescrevo os filtros encadeados default definidos pelo Spring Security,
     // de modo que nao mais precisade autenticacao, por exemplo
     @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

//        1) all request should be authenticated
        httpSecurity.authorizeHttpRequests(
                auth -> auth.anyRequest().authenticated()
        );
//        2) If a request is not authenticated, a web page is shown
         httpSecurity.httpBasic(withDefaults());
//        3)CSRF -> POST, PUT
         httpSecurity.csrf().disable();
//
        return httpSecurity.build();
    }
}
