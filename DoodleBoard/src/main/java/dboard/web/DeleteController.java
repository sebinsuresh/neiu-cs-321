package dboard.web;

import dboard.DoodlePost;
import dboard.data.DoodlePostRepository;
import dboard.data.DoodleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/delete")
public class DeleteController {

    private DoodlePostRepository doodlePostRepo;
    private DoodleRepository doodleRepo;

    @Autowired
    public DeleteController(DoodleRepository dr, DoodlePostRepository dpr){
        this.doodlePostRepo = dpr;
        this.doodleRepo = dr;
    }

    @PostMapping("/{postid}")
    public String deletePost(@PathVariable Long postid){
        DoodlePost tempDP = doodlePostRepo.findById(postid).get();
        doodlePostRepo.delete(tempDP);
        doodleRepo.delete(tempDP.getContent());
        return "redirect:/feed";
    }

}
