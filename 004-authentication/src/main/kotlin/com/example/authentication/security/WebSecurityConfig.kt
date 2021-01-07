package com.example.authentication.security

import com.example.authentication.service.UserService
import com.example.authentication.service.UserSessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var authFailureHandler: AuthFailureHandler

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userSessionService: UserSessionService

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .addFilter(AuthenticationFilter(authenticationManager(), jwtUtils, userService, userSessionService))
                .addFilter(CookieAuthenticationFilter(authenticationManager(), jwtUtils, userService, userSessionService))
                .exceptionHandling().authenticationEntryPoint(authFailureHandler)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

    }

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}