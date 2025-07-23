package vostrikov.pet.twitter.services

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service
import vostrikov.pet.twitter.model.dto.UserDto
import vostrikov.pet.twitter.model.dto.toUserEntity
import vostrikov.pet.twitter.model.entities.toUserDto
import vostrikov.pet.twitter.repositories.UserAccountsRepository


interface UserAccountsService {
    fun findUserAccountByUsername(username: String): UserDto
    fun createUser(userDto: UserDto, request: HttpServletRequest)
    fun findAll(authentication: Authentication): List<UserDto>
}

@Service
class UserAccountsServiceImpl(
    private val userAccountsRepository: UserAccountsRepository,
    private val userDetailsManager: UserDetailsManager,
    private val passwordEncoder: PasswordEncoder,
) : UserAccountsService {

    private val log = KotlinLogging.logger {}


    override fun findUserAccountByUsername(username: String): UserDto {
        val userEntity = userAccountsRepository.findByUsername(username)
            ?: throw RuntimeException("User not found with username: $username")
        return userEntity.toUserDto()
    }

    override fun createUser(userDto: UserDto, request: HttpServletRequest) {
        log.debug { "Creating new user: $userDto" }
        require(!userDto.username.isNullOrBlank() || !userDto.password.isNullOrBlank()) { "Need to feel username or password" }
        require(!userDto.name.isNullOrBlank()) { "Need to feel user name" }
        val user =
            User(userDto.username, passwordEncoder.encode(userDto.password), AuthorityUtils.createAuthorityList("read"))
        userDetailsManager.createUser(user)
        userAccountsRepository.save(userDto.toUserEntity())
        // authenticate newly created user
        authenticateNewlyCreatedUser(userDto, user, request)
    }

    override fun findAll(authentication: Authentication): List<UserDto> {
        return userAccountsRepository.findByOrderByName().map { it.toUserDto() }.filterNot { it.username == authentication.name }
    }

    private fun authenticateNewlyCreatedUser(userDto: UserDto, user: User, request: HttpServletRequest) {
        val auth: Authentication = UsernamePasswordAuthenticationToken(userDto.username, null, user.authorities)
        val context = SecurityContextHolder.getContext()
        context.setAuthentication(auth)
        //this step is important, otherwise the new login is not in session which is required by Spring Security
        request.session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        )
    }
}

