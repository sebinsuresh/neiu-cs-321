package dboard.security;

import dboard.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm(){
        return "/register";
    }

    @ModelAttribute
    public void addRegistrationFormToModel(Model model){
        model.addAttribute(new RegistrationForm());
    }

    @PostMapping
    public String processRegistration(@SessionAttribute @Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
                                    Errors errors){
        if(errors.hasErrors())
            return "/register";

        try{
            userRepo.save(registrationForm.toUser(passwordEncoder));
            return "redirect:/login";

        } catch(DataIntegrityViolationException ex){
            errors.rejectValue("username",
                    "invalidUserName",
                    "Username already taken. Please try another username");
            return "/register";

        } catch (Exception ex){
            log.error("Exception: " + ex.getMessage());
            return "/register";
        }
    }
}
