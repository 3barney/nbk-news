package com.nbk.test.news.infrastructure.adapter.outgoing.authentication

import com.nbk.test.news.application.services.UserService
import com.nbk.test.news.infrastructure.adapter.outgoing.authentication.jwt.JwtTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableWebSecurity
@Configuration
class ApplicationSecurity(private val userService: UserService) {

    @Bean
    fun userDetailsService(): UserDetailsService? {
        return UserDetailsServiceImpl(userService);
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider? {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Throws(Exception::class)
    protected fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }


    @Bean
    fun authManager(httpSecurity: HttpSecurity): AuthenticationManager {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService<UserDetailsService>(userDetailsService())
            .passwordEncoder(passwordEncoder())
            .and()
            .authenticationProvider(authenticationProvider())
            .build()
    }

    @Autowired
    private val jwtTokenFilter: JwtTokenFilter? = null

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            it.requestMatchers("/api/users/**").permitAll()
            it.requestMatchers("/error").permitAll()
            it.requestMatchers("/api/v1/login/**").permitAll()
            it.requestMatchers("/api/v1/news/**").hasAnyAuthority ("USER")
            it.requestMatchers("api/v1/file-download/**").hasAnyAuthority ("USER")
            it.anyRequest().authenticated()
        }
        http.formLogin { it.disable() }
        http.logout { it.disable() }
        http.cors{ it.disable() }
        http.csrf { it.disable() }

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}