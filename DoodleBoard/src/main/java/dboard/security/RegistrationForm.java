package dboard.security;

import dboard.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegistrationForm {

    @NotNull(message = "The username cannot be empty")
    @NotEmpty(message = "The username cannot be empty")
    @Size(min=5, message="The username must be at least 5 characters")
    private String username;

    @NotNull(message = "The password cannot be empty")
    @NotEmpty(message = "The password cannot be empty")
    @Size(min=6, message="The password must be at least 6 characters")
    private String password;

    @NotNull(message = "The email cannot be empty")
    @NotEmpty(message = "The email cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+", message="Format your email correctly (email@provider.tld)")
    private String email;

    @NotNull(message = "The name cannot be empty")
    @NotEmpty(message = "The name cannot be empty")
    private String fullname;

    private String bio;

    public User toUser(PasswordEncoder passwordEncoder){
        User newUser = new User(passwordEncoder.encode(this.password), this.email);
        newUser.setUsername(this.username);
        newUser.setFullname(this.fullname);
        if(this.bio != null)
            newUser.setBio(this.bio);
        return newUser;
    }
}
