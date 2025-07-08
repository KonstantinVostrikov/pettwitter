package vostrikov.pet.twitter.model.entities

import jakarta.persistence.*
import vostrikov.pet.twitter.model.dto.PostDto
import java.time.LocalDateTime


@Entity
@Table(name = "posts")
class PostEntity(

    @Id
    @Column(length = 36, nullable = false)
    var id: String = "",

    @Column(length = 1024, nullable = true)
    var content: String? = null,

    @ManyToOne
    @JoinColumn(name = "author")
    var author: UserEntity,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToMany(targetEntity = UserEntity::class)
    @JoinTable(
        name = "posts_likes_counter",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var likesCounter: Set<UserEntity>? = null,
)


fun PostEntity.toDto() = PostDto(
    id = this.id,
    content = this.content,
    author = this.author.name,
    updatedAt = this.updatedAt,
    likesCounter = this.likesCounter?.size ?: 0
)