package dboard.web;

import dboard.DoodlePost;
import dboard.Palette;
import dboard.User;
import dboard.data.DoodlePostRepository;
import dboard.data.DoodleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/myposts")
@Slf4j
public class MyPostsController {

    private final DoodlePostRepository doodPostRepo;
    private final DoodleRepository doodRepo;
    @Autowired
    private DoodlePostProperty doodlePostProperty;

    public MyPostsController(DoodlePostRepository doodPostRepo, DoodleRepository doodRepo, DoodlePostProperty doodlePostProperty){
        this.doodPostRepo = doodPostRepo;
        this.doodRepo = doodRepo;
        this.doodlePostProperty = doodlePostProperty;
    }

    @GetMapping
    public String showMyPosts(Model model, @AuthenticationPrincipal User user){
        return "myposts";
    }

    @ModelAttribute
    public void addDoodles(Model model, @AuthenticationPrincipal User user){
        Pageable pageable = PageRequest.of(0, doodlePostProperty.getFeedSize());

        model.addAttribute("palette", Arrays.asList(Palette.PALETTE));
        List<DoodlePost> allPosts = doodPostRepo.findAllByUserOrderByPostedAtDesc(user, pageable);
        model.addAttribute("posts", allPosts);

        String username = user.getUsername();
        model.addAttribute("username", username);

        // This check is required as EditController might have already added a DoodlePost object
        // as a redirect attribute, if there was an error in the post.
        if(!model.containsAttribute("hasError")) {
            model.addAttribute("doodlepost", new DoodlePost());
        }
    }
}
