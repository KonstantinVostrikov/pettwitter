package vostrikov.pet.twitter.services

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import vostrikov.pet.twitter.model.dto.UserDto
import vostrikov.pet.twitter.model.dto.toUserEntity
import vostrikov.pet.twitter.model.entities.toUserDto
import vostrikov.pet.twitter.repositories.UserAccountsRepository


interface UserAccountsService {
    fun findUserAccountByUsername(username: String): UserDto
    fun createUser(userDto: UserDto, request: HttpServletRequest)
    fun findPeople(authentication: Authentication, pageable: Pageable): Page<UserDto>
    fun save(authentication: Authentication, userDto: UserDto?): UserDto
    fun savePhoto(userDto: UserDto, multipartFile: MultipartFile)
}

@Service
class UserAccountsServiceImpl(
    private val userAccountsRepository: UserAccountsRepository,
    private val userDetailsManager: UserDetailsManager,
    private val passwordEncoder: PasswordEncoder,
    private val fileUploadServiceImpl: FileUploadServiceImpl,
) : UserAccountsService {

    private val log = LoggerFactory.getLogger(javaClass)


    override fun findUserAccountByUsername(username: String): UserDto {
        val userEntity = userAccountsRepository.findByUsername(username)
            ?: throw RuntimeException("User not found with username: $username")
        return userEntity.toUserDto()
    }

    override fun createUser(userDto: UserDto, request: HttpServletRequest) {
        log.debug("Creating new user:{}", userDto.toString())
        require(!userDto.username.isNullOrBlank() ) { "Need to feel username" }
        require(!userDto.password.isNullOrBlank() ) { "Need to feel password" }
        require(!userDto.name.isNullOrBlank()) { "Need to feel user name" }
        val user =
            User(userDto.username, passwordEncoder.encode(userDto.password), AuthorityUtils.createAuthorityList("read"))
        userDetailsManager.createUser(user)
        userAccountsRepository.save(userDto.toUserEntity())
        // authenticate newly created user
        authenticateNewlyCreatedUser(userDto, user, request)
    }

    override fun findPeople(authentication: Authentication, pageable: Pageable): Page<UserDto> {
        return userAccountsRepository.findByUsernameNotOrderByName(pageable, authentication.name).map { it.toUserDto() }
    }

    override fun save(authentication: Authentication, userDto: UserDto?): UserDto {
        log.debug("Changing user: {} by {}", userDto, authentication.name)
        require(!userDto?.username.isNullOrBlank()) { "Username is required" }
        require(authentication.name == userDto!!.username) { "Users can change only self profile" }

        val userEntity = userAccountsRepository.findByUsername(userDto.username!!)
        val user = userAccountsRepository.save(userDto.toUserEntity(userEntity!!.id))
        return user.toUserDto()
    }

    override fun savePhoto(userDto: UserDto, multipartFile: MultipartFile) {
        userDto.photo = "images/" + fileUploadServiceImpl.uploadFileToDb(multipartFile)
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

