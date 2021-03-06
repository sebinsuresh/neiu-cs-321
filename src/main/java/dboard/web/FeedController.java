package dboard.web;

import dboard.DoodlePost;
import dboard.Palette;
import dboard.User;
import dboard.data.DoodlePostRepository;
import dboard.data.DoodleRepository;
import dboard.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/feed")
public class FeedController {

    DoodlePostRepository doodlePostRepo;
    DoodleRepository doodleRepo;
    UserRepository userRepo;
    private DoodlePostProperty doodlePostProperty;

    @Autowired
    public FeedController(DoodlePostRepository dpr, DoodleRepository dr, DoodlePostProperty dpp, UserRepository ur){
        this.doodlePostRepo = dpr;
        this.doodleRepo = dr;
        this.doodlePostProperty = dpp;
        this.userRepo = ur;
    }

    @GetMapping(value = {"", "/", "/page/1"})
    public String showFeedPosts(Model model, @AuthenticationPrincipal User user){
        addPostsToModel(model, 0, user);
        return "feed";
    }

    @GetMapping(value = {"/page/{pageNum}"})
    public String showFeedPosts(Model model, @AuthenticationPrincipal User user, @PathVariable Integer pageNum){
        long numPosts = doodlePostRepo.count();
        int feedSize = doodlePostProperty.getFeedSize();

        pageNum = pageNum - 1;

        if(pageNum * feedSize >= numPosts || pageNum < 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Page number out of bounds"
            );
        } else {
            addPostsToModel(model, pageNum, user);
        }

        return "feed";
    }

    public void addPostsToModel(Model model, int pageNum, User user){
        long numPosts = doodlePostRepo.count();
        int feedSize = doodlePostProperty.getFeedSize();

        int numPages = (int) Math.ceil((double)numPosts/feedSize);
        model.addAttribute("numpages", numPages);
        model.addAttribute("currpagenum", pageNum);

        model.addAttribute("palette", Arrays.asList(Palette.PALETTE));

        Pageable pageable = PageRequest.of(pageNum, feedSize);
        List<DoodlePost> allPosts = doodlePostRepo.findAllByOrderByPostedAtDesc(pageable);
        model.addAttribute("posts", allPosts);

        List<DoodlePost> likedPosts = userRepo.findById(user.getId()).get().getLikedPosts();
        List<Long> userLikedPostsIds = new ArrayList<>();
        for(DoodlePost post : allPosts){
            if(likedPosts.contains(post)){
                userLikedPostsIds.add(post.getPostId());
            }
        }
        model.addAttribute("likedPostIds", userLikedPostsIds);
    }

}
