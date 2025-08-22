package vostrikov.pet.twitter.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import vostrikov.pet.twitter.model.entities.ImageEntity

@Repository
interface ImageRepository : CrudRepository<ImageEntity, String> {
}