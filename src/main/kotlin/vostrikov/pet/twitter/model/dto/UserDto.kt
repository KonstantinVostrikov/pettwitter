package vostrikov.pet.twitter.model.dto

import vostrikov.pet.twitter.model.entities.UserEntity
import java.util.*

data class UserDto(
    val id: String = UUID.randomUUID().toString(),
    val username: String? = null,
    val name: String? = null,
    val password: String? = null,
)

fun UserDto.toUserEntity() = UserEntity(
    id = id,
    username = username ?: throw RuntimeException("username is missing"),
    name = name ?: throw RuntimeException("username is missing"),
)