package vostrikov.pet.twitter.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component


@Component
class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    private val log = KotlinLogging.logger {}

    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        log.info { "Login successful for the user :  ${authentication!!.name}" }
        response!!.sendRedirect("/feed")
    }
}