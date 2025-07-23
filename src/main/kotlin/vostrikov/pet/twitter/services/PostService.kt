package vostrikov.pet.twitter.services

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import vostrikov.pet.twitter.model.dto.PostDto
import vostrikov.pet.twitter.model.dto.toPostEntity
import vostrikov.pet.twitter.model.dto.validate
import vostrikov.pet.twitter.model.entities.toDto
import vostrikov.pet.twitter.repositories.PostRepository
import vostrikov.pet.twitter.repositories.UserAccountsRepository

interface PostService {
    fun createPost(post: PostDto): Boolean
    fun getPosts(pageable: Pageable): Page<PostDto>
    fun like(postId: String?, username: String?)
}

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userAccountsRepository: UserAccountsRepository
) : PostService {

    override fun createPost(post: PostDto): Boolean {
        post.validate()
        postRepository.save(post.toPostEntity())
        return true
    }

    override fun getPosts(pageable: Pageable): Page<PostDto> {
        return postRepository.findAByOrderByUpdatedAtDesc(pageable).map { it.toDto() }
    }

    override fun like(postId: String?, username: String?) {
        require(!(username.isNullOrBlank())) { "Username must be set" }
        require(!(postId.isNullOrBlank())) { "PostId must be set" }
        val user = userAccountsRepository.findByUsername(username) ?: throw throw UsernameNotFoundException("User not found with username: $username")
        val postEntity = postRepository.findByIdOrNull(postId) ?: throw ChangeSetPersister.NotFoundException()
        if (user in postEntity.likesCounter) postEntity.likesCounter.remove(user) else postEntity.likesCounter.add(user)
        postRepository.save(postEntity)
    }

}