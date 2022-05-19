package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        // TODO: 余裕があったらパスワードのハッシュ化するかも
        return NoOpPasswordEncoder.getInstance();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/list", true)
            .failureUrl("/login-error");

        http.authorizeRequests()
            .mvcMatchers("/add/**").hasAuthority("ADMIN")
            .mvcMatchers("/create/**").hasAuthority("ADMIN")
            .anyRequest().authenticated();
    }
}