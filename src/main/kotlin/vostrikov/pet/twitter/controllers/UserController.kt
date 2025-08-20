package vostrikov.pet.twitter.controllers

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import vostrikov.pet.twitter.model.dto.UserDto
import vostrikov.pet.twitter.services.FileUploadServiceImpl
import vostrikov.pet.twitter.services.UserAccountsService
import vostrikov.pet.twitter.uploadDir


@Controller
@RequestMapping("/users")
class UserController(
    private val userAccountsService: UserAccountsService,
    private val fileUploadServiceImpl: FileUploadServiceImpl
) {
    private val log = KotlinLogging.logger {}

    @GetMapping("/signup")
    fun signUp(model: Model): String {
        model.addAttribute("user", UserDto())
        return "sign-up"
    }


    @PostMapping("/create-user")
    fun createUser(@ModelAttribute userDto: UserDto, request: HttpServletRequest): String {
        return try {
            userAccountsService.createUser(userDto, request)
            "redirect:users/my-profile"

        } catch (ex: Exception) {
            log.error(ex.message, ex)
            "redirect:/feed"
        }
    }


    @GetMapping("/find-people")
    fun findPeople(
        model: Model,
        authentication: Authentication,
        @PageableDefault(size = 10) pageable: Pageable
    ): String {
        val users = userAccountsService.findPeople(authentication, pageable)
        model.addAttribute("page", users)
        return "users/find-people"
    }


    @GetMapping("/my-profile")
    fun myProfile(model: Model, authentication: Authentication): String {
        val user = userAccountsService.findUserAccountByUsername(authentication.name)
        model.addAttribute("user", user)
        return "users/my-profile"
    }


    @PostMapping("/save-user-profile")
    fun saveUser(
        model: Model,
        authentication: Authentication,
        @ModelAttribute userDto: UserDto,
        @RequestParam("image") multipartFile: MultipartFile?
    ): String {
        if (multipartFile != null && !multipartFile.isEmpty) {
            val fileName = StringUtils.cleanPath(multipartFile.originalFilename.orEmpty())
            log.debug("File name is {}", fileName)
            userDto.photo = fileName
            val uploadDir = uploadDir + userDto.username;
            fileUploadServiceImpl.uploadFile(uploadDir, fileName, multipartFile)
        }
        val user = userAccountsService.save(authentication, userDto)
        model.addAttribute("user", user)
        model.addAttribute("saved", true)
        return "users/my-profile"
    }
}