package dboard.web;

import dboard.DoodlePost;
import dboard.Palette;
import dboard.User;
import dboard.data.DoodlePostRepository;
import dboard.data.DoodleRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.valuecontext.ValueContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/myposts")
@Slf4j
public class MyPostsController {

    private final DoodlePostRepository doodPostRepo;
    private final DoodleRepository doodRepo;
    private DoodlePostProperty doodlePostProperty;

    @Autowired
    public MyPostsController(DoodlePostRepository doodPostRepo, DoodleRepository doodRepo, DoodlePostProperty doodlePostProperty){
        this.doodPostRepo = doodPostRepo;
        this.doodRepo = doodRepo;
        this.doodlePostProperty = doodlePostProperty;
    }

    @GetMapping(value= {"", "/"})
    public String showMyPosts(Model model, @AuthenticationPrincipal User user){
        addPostsToModel(model, user, 0);
        return "myposts";
    }

    @GetMapping(value={"/page/{pageNum}"})
    public String showMyPosts(Model model, @AuthenticationPrincipal User user, @PathVariable Integer pageNum){
        int numPostsByUser = doodPostRepo.countByUser(user).intValue();
        int feedSize = doodlePostProperty.getFeedSize();

        pageNum = pageNum-1;

        if(pageNum * feedSize >= numPostsByUser || pageNum < 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Page number out of bounds"
            );
        } else {
            addPostsToModel(model, user, pageNum);
        }

        return "myposts";
    }

    public void addPostsToModel(Model model, User user, int pageNum){

        int numPostsByUser = doodPostRepo.countByUser(user).intValue();
        int feedSize = doodlePostProperty.getFeedSize();

        int numPages = (int) Math.ceil((double)numPostsByUser/feedSize);
        model.addAttribute("numpages", numPages);
        model.addAttribute("currpagenum", pageNum);

        Pageable pageable = PageRequest.of(pageNum, feedSize);

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
