package dboard.web;

import dboard.DoodlePost;
import dboard.Palette;
import dboard.data.DoodlePostRepository;
import dboard.data.DoodleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    public FeedController(DoodlePostRepository doodPostRepo, DoodleRepository doodRepo){
        this.doodPostRepo = doodPostRepo;
        this.doodRepo = doodRepo;
    }

    @GetMapping
    public String showFeed(){
        return "feed";
    }

    @ModelAttribute
    public void addDoodles(Model model){
        model.addAttribute("palette", Arrays.asList(Palette.PALETTE));
        List<DoodlePost> allPosts = doodPostRepo.findAllByOrderByPostedAtDesc();
        model.addAttribute("posts", allPosts);
    }
}
