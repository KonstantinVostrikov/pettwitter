package vostrikov.pet.twitter.model.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import vostrikov.pet.twitter.model.dto.UserDto
import vostrikov.pet.twitter.uploadDir

@Entity
@Table(name = "user_accounts")
class UserEntity(
    @Id
    @Column(length = 36, nullable = false)
    var id: String = "",

    @Column(length = 50, nullable = false)
    var username: String = "",

    @Column(length = 40, nullable = false)
    var name: String = "",

    @Column(length = 255)
    var photo: String? = ""

//    @ManyToMany(targetEntity = PostEntity::class)
//    var  likes : Set<PostEntity>
)

fun UserEntity.toUserDto() = UserDto(
    id = id,
    username = username,
    name = name,
    photo = if (!photo.isNullOrBlank()) "$uploadDir$username/$photo" else "assets/images/user.png"
)