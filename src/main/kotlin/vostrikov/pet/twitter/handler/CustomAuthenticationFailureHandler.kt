package vostrikov.pet.twitter.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {
    private val log = KotlinLogging.logger {}

    override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {
        log.error { "Login failed for username=${exception!!.authenticationRequest.principal}  due to : ${exception.message}" }
        response!!.sendRedirect("/login?error=true")
    }
}