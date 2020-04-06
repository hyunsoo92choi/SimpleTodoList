package com.hschoi.todo.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry


/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/04
 */
@Configuration
@EnableWebSecurity
class SecurityConfig() : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/**", "/h2-console/**" ).permitAll()
            .anyRequest().authenticated()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/configuration/security",
                "/h2-console/**",
                "/webjars/**"
            )
    }

}
