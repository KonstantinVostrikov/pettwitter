package vostrikov.pet.twitter.controllers

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import vostrikov.pet.twitter.model.dto.PostDto
import vostrikov.pet.twitter.services.PostService
import vostrikov.pet.twitter.services.UserAccountsService


@Controller
class PostController(
    private val postService: PostService,
    private val userAccountsService: UserAccountsService,
) {

    @RequestMapping("/")
    fun home(): String {
        return "redirect:/feed"
    }

    @GetMapping("/feed")
    fun posts(model: Model): String {
        val posts = postService.getPosts()
        model.addAttribute("posts", posts)
        model.addAttribute("post", PostDto())
        return "feed"
    }

    @PostMapping("/create-post")
    fun createPost(@ModelAttribute post: PostDto, @AuthenticationPrincipal  userDetails: UserDetails): String {
        val userDto = userAccountsService.findUserAccountByUsername(userDetails.username)
        post.authorNickname = userDto.id
        postService.createPost(post)
        return "redirect:/feed"
    }


    @GetMapping("/an")
    fun index(): String {
        return "index"
    }
}