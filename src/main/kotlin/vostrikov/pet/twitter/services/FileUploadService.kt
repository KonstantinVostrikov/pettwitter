package vostrikov.pet.twitter.services

import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import vostrikov.pet.twitter.model.entities.ImageEntity
import vostrikov.pet.twitter.repositories.ImageRepository
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

interface FileUploadService {
    fun uploadFile(uploadDir: String, filename: String, multipartFile: MultipartFile)
    fun uploadFileToDb(multipartFile: MultipartFile) : String
    fun downloadFile(fileId: String): ImageEntity
}

@Service
class FileUploadServiceImpl(
    private val imageRepository: ImageRepository,
) : FileUploadService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun uploadFile(uploadDir: String, filename: String, multipartFile: MultipartFile) {
        val path = Paths.get(uploadDir)

        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }

        try {
            val inputStream = multipartFile.inputStream
            val filePath = path.resolve(filename)
            log.debug("saving into {}", filePath)
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            log.error("Error while creating file", e)
            throw e
        }
    }

    override fun uploadFileToDb(multipartFile: MultipartFile): String {
        val fileName = multipartFile.originalFilename ?: throw RuntimeException("MultipartFile originalFilename is empty or null")
        log.debug ("saving file: {} into db", fileName)
        val imageEntity = ImageEntity(
            id = UUID.randomUUID().toString(),
            name = fileName,
            type = multipartFile.contentType.orEmpty(),
            fileSize = multipartFile.size,
            fileData = multipartFile.bytes
        )
        val entity = imageRepository.save(imageEntity)
        return entity.id
    }

    override fun downloadFile(fileId: String): ImageEntity {
        val file = imageRepository.findByIdOrNull(fileId) ?: throw FileNotFoundException("File with id:$fileId not found")
        return file
    }


}