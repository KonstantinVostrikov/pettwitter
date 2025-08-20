package vostrikov.pet.twitter.services

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

interface FileUploadService {
    fun uploadFile(uploadDir : String, filename: String, multipartFile: MultipartFile)
}

@Service
class FileUploadServiceImpl : FileUploadService {
    private val log = KotlinLogging.logger {}


    override fun uploadFile(uploadDir: String, filename: String, multipartFile: MultipartFile) {
        val path = Paths.get(uploadDir)

        if(!Files.exists(path)) {
            Files.createDirectories(path);
        }

        try {
            val inputStream = multipartFile.inputStream
            val filePath = path.resolve(filename)
            log.debug { "saving into $filePath" }
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        } catch(e: Exception) {
            log.error("Error while creating file", e)
            throw e;
        }
    }

}