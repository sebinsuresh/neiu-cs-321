package dboard.web;

import dboard.DoodlePost;
import dboard.Palette;
import dboard.data.DoodlePostRepository;
import dboard.data.DoodleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
public class DrawController {

    private final DoodlePostRepository dooprepository;
    private final DoodleRepository doodleRepository;

    @Autowired
    public DrawController(DoodlePostRepository dpr, DoodleRepository dr){
        this.dooprepository = dpr;
        this.doodleRepository = dr;
    }

    @GetMapping("/draw")
    public String showDraw(){
        return "draw";
    }

    @PostMapping("/draw")
    public String submitDraw(@Valid @ModelAttribute("doodlepost") DoodlePost doodlepost, Errors errors){
        if(errors.hasErrors()){
            return "/draw";
        }

        doodleRepository.save(doodlepost.getContent());
        dooprepository.save(doodlepost);

        return "redirect:/submission";
    }

    // Adds the colors of the palette as a list to the Model
    @ModelAttribute
    public void addPalette(Model model){
        model.addAttribute("doodlepost", new DoodlePost());
        model.addAttribute("palette", Arrays.asList(Palette.PALETTE));
    }
}
