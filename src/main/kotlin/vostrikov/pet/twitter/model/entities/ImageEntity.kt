package vostrikov.pet.twitter.model.entities

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "image")
class ImageEntity(
    @Id
    @Column(length = 36, nullable = false)
    var id: String = "",

    @Column(length = 120, nullable = true)
    var name: String = "",

    @Column(length = 120, nullable = true)
    var type: String = "",

    @Column(nullable = false)
    var fileSize: Long? = null,

    @Lob
    @Column(nullable = true)
    @JdbcTypeCode(SqlTypes.BINARY)
    var fileData: ByteArray? = null,
) {


}