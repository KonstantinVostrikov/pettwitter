package vostrikov.pet.twitter.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component


@Component
class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        log.info("Login successful for the user:{}", authentication!!.name)
        response!!.sendRedirect("/feed")
    }
}