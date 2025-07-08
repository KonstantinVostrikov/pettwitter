package vostrikov.pet.twitter.model.dto

import vostrikov.pet.twitter.model.entities.UserEntity

data class UserDto(
    val id: String,
    val username: String,
    val name: String,
)

fun UserDto.toUserEntity() = UserEntity(
    id = id,
    username = username,
    name = name,
)