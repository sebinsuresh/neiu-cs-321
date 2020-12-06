package dboard.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/submission")
public class SubmissionController {
    @GetMapping
    public String getSubmissionPage(){
        return "submission";
    }
}
