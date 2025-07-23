package vostrikov.pet.twitter.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import vostrikov.pet.twitter.model.entities.PostEntity

@Repository
interface PostRepository : CrudRepository<PostEntity, String> {

    fun findAByOrderByUpdatedAtDesc(pageable: Pageable): Page<PostEntity>

}