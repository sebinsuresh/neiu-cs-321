package dboard.web;

import dboard.DoodlePost;
import dboard.Palette;
import dboard.User;
import dboard.data.DoodlePostRepository;
import dboard.data.DoodleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/feed")
@Slf4j
public class FeedController {

    private final DoodlePostRepository doodPostRepo;
    private final DoodleRepository doodRepo;
    @Autowired
    private DoodlePostProperty doodlePostProperty;

    public FeedController(DoodlePostRepository doodPostRepo, DoodleRepository doodRepo, DoodlePostProperty doodlePostProperty){
        this.doodPostRepo = doodPostRepo;
        this.doodRepo = doodRepo;
        this.doodlePostProperty = doodlePostProperty;
    }

    @GetMapping
    public String showFeed(){
        // The posts being shown are limited to the value in doodlefeed.posts.pageSize,
        // before adding to the model, in the addDoodles() method
        return "feed";
    }

    @ModelAttribute
    public void addDoodles(Model model, @AuthenticationPrincipal User user){
        Pageable pageable = PageRequest.of(0, doodlePostProperty.getFeedSize());

        model.addAttribute("palette", Arrays.asList(Palette.PALETTE));
        List<DoodlePost> allPosts = doodPostRepo.findAllByUserOrderByPostedAtDesc(user, pageable);
        model.addAttribute("posts", allPosts);

        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // User user = (User)auth.getPrincipal();
        String username = user.getUsername();
        model.addAttribute("username", username);

        // This check is required as EditController might have already added a DoodlePost object,
        // if there was an error in the post.
        if(!model.containsAttribute("hasError")) {
            model.addAttribute("doodlepost", new DoodlePost());
        }
    }
}
