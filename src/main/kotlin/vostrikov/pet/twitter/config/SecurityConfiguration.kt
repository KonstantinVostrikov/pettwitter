package vostrikov.pet.twitter.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.sql.DataSource


@Configuration
class SecurityConfiguration(
    private val authenticationSuccessHandler: AuthenticationSuccessHandler,
    private val authenticationFailureHandler: AuthenticationFailureHandler
) {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(
                        "/assets/**",
                        "/login/**",
                        "/users/signup",
                        "/users/create-user"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { formLoginConfig ->
                formLoginConfig
                    .loginPage("/login/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
            }

        return http.build()
    }

    @Bean
    fun userDetailsService(dataSource: DataSource): JdbcUserDetailsManager {
        return JdbcUserDetailsManager(dataSource)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}