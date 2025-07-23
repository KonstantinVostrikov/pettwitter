package vostrikov.pet.twitter.controllers

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import vostrikov.pet.twitter.model.dto.UserDto
import vostrikov.pet.twitter.services.UserAccountsService

@Controller
@RequestMapping("/users")
class UserController(
    private val userAccountsService: UserAccountsService
) {
    private val log = KotlinLogging.logger {}

    @GetMapping("/signup")
    fun signUp(model: Model): String {
        model.addAttribute("user", UserDto())
        return "sign_up"
    }


    @PostMapping("/create-user")
    fun createUser(@ModelAttribute userDto: UserDto, request: HttpServletRequest): String {
        return try {
            userAccountsService.createUser(userDto, request)
            // todo return profile page
            "redirect:/feed"

        } catch (ex: Exception) {
            log.error(ex.message, ex)
            "redirect:/feed"
        }
    }


    @GetMapping("/find-people")
    fun findPeople(model: Model, authentication: Authentication): String {
        val users = userAccountsService.findAll(authentication)
        model.addAttribute("users", users)
        return "users/findPeople"
    }
}