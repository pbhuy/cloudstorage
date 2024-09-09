package com.udacity.jwdnd.course1.cloudstorage.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/signup", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()

                .and().headers().frameOptions().disable()

                .and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()

                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
}
