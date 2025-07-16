package vostrikov.pet.twitter.controllers

import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
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
//    fun createPost(@ModelAttribute post: PostDto, @AuthenticationPrincipal  userDetails: UserDetails): String {
    fun createPost(@ModelAttribute post: PostDto, authentication: Authentication): String {
        val userDto = userAccountsService.findUserAccountByUsername(authentication.name)
        post.authorNickname = userDto.id
        postService.createPost(post)
        return "redirect:/feed"
    }

    @GetMapping("/post/like/{id}")
    fun likePost(@PathVariable id: String, authentication: Authentication): String {
        postService.like(id, authentication.name)
        return "redirect:/feed"
    }



}