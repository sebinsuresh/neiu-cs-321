package dboard.web;

import dboard.DoodlePost;
import dboard.User;
import dboard.data.DoodlePostRepository;
import dboard.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/toggleLike")
public class LikePostsController {

    DoodlePostRepository doodlePostRepo;
    UserRepository userRepo;

    @Autowired
    public LikePostsController(DoodlePostRepository dpr, UserRepository ur){
        this.doodlePostRepo = dpr;
        this.userRepo = ur;
    }

    @PostMapping("/{postId}")
    public String toggleLike(@PathVariable Long postId,
                             @AuthenticationPrincipal User user,
                             @ModelAttribute(value = "page") String page,
                             @ModelAttribute(value="pageNum") int pageNum){

        DoodlePost likedPost = doodlePostRepo.findById(postId).get();
        List<DoodlePost> likedPosts = userRepo.findById(user.getId()).get().getLikedPosts();

        if(likedPosts.contains(likedPost)){
            likedPost.decrLikes();
            likedPosts.remove(likedPost);
        } else {
            likedPost.incrLikes();
            likedPosts.add(likedPost);
        }
        user.setLikedPosts(likedPosts);
        doodlePostRepo.save(likedPost);
        userRepo.save(user);

        return "redirect:/" + page + "/page/" + (pageNum+1);
    }
}
