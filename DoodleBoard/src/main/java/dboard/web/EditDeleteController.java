package dboard.web;

import dboard.Doodle;
import dboard.DoodlePost;
import dboard.Palette;
import dboard.data.DoodlePostRepository;
import dboard.data.DoodleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;

@Slf4j
@Controller
@RequestMapping("/edit")
public class EditDeleteController {

    DoodleRepository doodleRepo;
    DoodlePostRepository doodlePostRepo;

    @Autowired
    public EditDeleteController(DoodleRepository doodleRepo, DoodlePostRepository doodlePostRepo){
        this.doodleRepo = doodleRepo;
        this.doodlePostRepo = doodlePostRepo;
    }

    @PostMapping("/{postId}")
    public String editDoodlePost(@PathVariable Long postId,
                                 Model model,
                                 RedirectAttributes redirectAttrs,
                                 @ModelAttribute(name = "doodlepost") @Valid DoodlePost editedPost,
                                 Errors errors){
        if(errors.hasErrors()){
            redirectAttrs.addFlashAttribute("palette", Arrays.asList(Palette.PALETTE));
            redirectAttrs.addFlashAttribute("hasError", true);
            redirectAttrs.addFlashAttribute("errors", errors);
            editedPost.setPostId(postId);
            redirectAttrs.addFlashAttribute("doodlepost", editedPost);
            return "redirect:/feed";
        }

        DoodlePost newPost = doodlePostRepo.findById(postId).get();
        Doodle newDoodle = newPost.getContent();
        newPost.setTitle(editedPost.getTitle());
        newDoodle.setData(editedPost.getContent().getData());
        doodlePostRepo.save(newPost);
        doodleRepo.save(newDoodle);

        return "redirect:/feed";
    }
}
