package vostrikov.pet.twitter.controllers

import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vostrikov.pet.twitter.services.FileUploadService


@RestController
@RequestMapping("/images")
class ImageController(
    private val fileUploadService: FileUploadService
) {


    @GetMapping("/{id}")
    fun getImage(@PathVariable id: String): ResponseEntity<InputStreamResource> {
        val imageEntity = fileUploadService.downloadFile(id)
        val inputStreamResource = InputStreamResource(imageEntity.fileData!!.inputStream())
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(imageEntity.type))
            .body(inputStreamResource)
    }
}