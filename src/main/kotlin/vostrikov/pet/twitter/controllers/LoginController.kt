package vostrikov.pet.twitter.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("login")
class LoginController {

    @GetMapping("/login")
    fun displayLoginPage(
        @RequestParam(value = "error", required = false) error: String?,
        @RequestParam(value = "logout", required = false) logout: String?,
        model: Model
    ): String {
        var errorMessge: String? = null
        if (null != error) {
            errorMessge = "Username or Password is incorrect!"
        }
        if (null != logout) {
            errorMessge = "You have been successfully logged out!"
        }
        model.addAttribute("errorMessge", errorMessge)
        return "login"
    }
}