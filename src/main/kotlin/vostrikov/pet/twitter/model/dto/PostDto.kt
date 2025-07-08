package vostrikov.pet.twitter.model.dto

import vostrikov.pet.twitter.model.entities.PostEntity
import vostrikov.pet.twitter.model.entities.UserEntity
import java.time.LocalDateTime
import java.util.*

data class PostDto(
    var id: String = "",
    var content: String? = null,
    var author: String? = null,
    var updatedAt: LocalDateTime? = null,
    var likesCounter: Int = 0
)

fun PostDto.toPostEntity() = PostEntity(
    id = id.ifBlank { UUID.randomUUID().toString() },
    content = content,
    // todo: real user name
    author = UserEntity(id = author!!)
)

fun PostDto.validate() {
    // todo: need to handle that exception on front
    require(!content.isNullOrBlank()) { "Content must not be blank" }
    require(!author.isNullOrBlank()) { "Author must not be blank" }
}