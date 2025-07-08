package vostrikov.pet.twitter.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import vostrikov.pet.twitter.model.entities.UserEntity


@Repository
interface UserAccountsRepository : CrudRepository<UserEntity, String> {
    fun findByUsername(username: String): UserEntity?
}