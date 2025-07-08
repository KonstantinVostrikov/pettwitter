package vostrikov.pet.twitter.services

import org.springframework.stereotype.Service
import vostrikov.pet.twitter.model.dto.PostDto
import vostrikov.pet.twitter.model.dto.toPostEntity
import vostrikov.pet.twitter.model.dto.validate
import vostrikov.pet.twitter.model.entities.toDto
import vostrikov.pet.twitter.repositories.PostRepository

interface PostService {
    fun createPost(post : PostDto) : Boolean
    fun getPosts(): List<PostDto>
}

@Service
class PostServiceImpl(
    private val postRepository: PostRepository
) : PostService {

    override fun createPost(post: PostDto): Boolean {
        post.validate()
        postRepository.save(post.toPostEntity())
        return true
    }

    override fun getPosts(): List<PostDto> {
        return postRepository.findAByOrderByUpdatedAtDesc().map { it.toDto() }
    }

}