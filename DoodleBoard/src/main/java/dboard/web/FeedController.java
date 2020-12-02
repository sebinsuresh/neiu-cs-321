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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/feed")
public class FeedController {

    DoodlePostRepository doodlePostRepo;
    DoodleRepository doodleRepo;
    private DoodlePostProperty doodlePostProperty;

    @Autowired
    public FeedController(DoodlePostRepository dpr, DoodleRepository dr, DoodlePostProperty dpp){
        this.doodlePostRepo = dpr;
        this.doodleRepo = dr;
        this.doodlePostProperty = dpp;
    }

    @GetMapping(value = {"", "/", "/page/1"})
    public String showFeedPosts(Model model, @AuthenticationPrincipal User user){
        addPostsToModel(model, 0);
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
            addPostsToModel(model, pageNum);
        }

        return "feed";
    }

    public void addPostsToModel(Model model, int pageNum){
        long numPosts = doodlePostRepo.count();
        int feedSize = doodlePostProperty.getFeedSize();

        int numPages = (int) Math.ceil((double)numPosts/feedSize);
        model.addAttribute("numpages", numPages);
        model.addAttribute("currpagenum", pageNum);

        Pageable pageable = PageRequest.of(pageNum, feedSize);

        model.addAttribute("palette", Arrays.asList(Palette.PALETTE));
        List<DoodlePost> allPosts = doodlePostRepo.findAll(pageable);
        model.addAttribute("posts", allPosts);
    }

}
