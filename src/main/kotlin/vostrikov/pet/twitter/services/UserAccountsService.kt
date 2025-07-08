package vostrikov.pet.twitter.services

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import vostrikov.pet.twitter.model.dto.UserDto
import vostrikov.pet.twitter.model.entities.UserEntity
import vostrikov.pet.twitter.repositories.UserAccountsRepository

interface UserAccountsService {
    fun findUserAccountByUsername(username: String) : UserDto
}

@Service
class UserAccountsServiceImpl(private val userAccountsRepository: UserAccountsRepository) : UserAccountsService {


    override fun findUserAccountByUsername(username: String) : UserDto{
        val userEntity = userAccountsRepository.findByUsername(username) ?: throw UsernameNotFoundException("User not found with username: $username")
        return userEntity.toUserDto()
    }
}

private fun UserEntity.toUserDto()  = UserDto(id = id, username =  username, name =  name)
