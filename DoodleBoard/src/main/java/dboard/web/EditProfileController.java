package dboard.web;

import dboard.User;
import dboard.data.UserRepository;
import dboard.security.RegistrationForm;
import dboard.security.UserRepositoryUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/editprofile")
public class EditProfileController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EditProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getEditProfilePage(@AuthenticationPrincipal User user, Model model){
        RegistrationForm regForm = new RegistrationForm();
        regForm.setUsername(user.getUsername());
        regForm.setPassword(user.getPassword());
        regForm.setEmail(user.getEmail());
        regForm.setFullname(user.getFullname());
        regForm.setBio(user.getBio());
        model.addAttribute("regForm", regForm);
        return "editprofile";
    }

    @PostMapping
    public String updateUserProfile(@Valid @ModelAttribute(value="regForm") RegistrationForm regForm,
                                    Errors errors,
                                    @AuthenticationPrincipal User sessionUser){
        if(errors.hasErrors()){
            // If all the errors are related to empty password string, ignore these errors
            boolean allErrorsAreEmptyPass = true;
            if(regForm.getPassword().equals("")){
                for(FieldError error : errors.getFieldErrors()){
                    if(!error.getField().toLowerCase().equals("password")){
                        allErrorsAreEmptyPass = false;
                    }
                }
            } else {
                allErrorsAreEmptyPass = false;
            }

            if(!allErrorsAreEmptyPass){
                return "editprofile";
            }
        }

        User userFromDb = userRepository.findById(sessionUser.getId()).get();
        if(!regForm.getPassword().equals("")){
            userFromDb.setPassword(passwordEncoder.encode(regForm.getPassword()));
        }
        userFromDb.setEmail(regForm.getEmail());
        sessionUser.setEmail(regForm.getEmail());
        userFromDb.setUsername(regForm.getUsername());
        sessionUser.setUsername(regForm.getUsername());
        userFromDb.setFullname(regForm.getFullname());
        sessionUser.setFullname(regForm.getFullname());
        if(regForm.getBio() != null){
            userFromDb.setBio(regForm.getBio());
            sessionUser.setBio(regForm.getBio());
        }
        userRepository.save(userFromDb);

        return "redirect:/feed";
    }
}
