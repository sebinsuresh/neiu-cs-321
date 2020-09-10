package dboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;

@Controller
public class DrawController {

    @GetMapping("/draw.html")
    public String showDraw(){
        return "draw";
    }

    // Adds the colors of the palette as a list to the Model
    @ModelAttribute
    public void addPalette(Model model){
        model.addAttribute("palette", Arrays.asList(DoodlePost.PALETTE));
    }
}
