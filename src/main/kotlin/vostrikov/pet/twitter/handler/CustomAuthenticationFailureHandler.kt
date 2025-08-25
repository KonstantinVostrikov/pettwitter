package vostrikov.pet.twitter.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        log.info(
            "Login failed for username:{} due to:{}",
            exception!!.authenticationRequest.principal,
            exception.message
        )
        if (exception is BadCredentialsException) {
            response!!.sendRedirect("/login/login?error=true")
        } else {
            response!!.sendRedirect("/error")
        }
    }
}