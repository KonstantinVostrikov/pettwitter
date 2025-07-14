package vostrikov.pet.twitter.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.sql.DataSource


@Configuration
class SecurityConfiguration {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/", "/feed", "/create-post" ).authenticated()
                    .requestMatchers("/assets/**", "/login/**").permitAll()
            }
            .formLogin { formLoginConfig ->
                formLoginConfig
                    .loginPage("/login")
                    .defaultSuccessUrl("/feed")
                    .failureUrl("/login?error=true")
            }

        return http.build()
    }

    @Bean
    fun userDetailsService(dataSource: DataSource): UserDetailsService {
        return JdbcUserDetailsManager(dataSource)
    }
}