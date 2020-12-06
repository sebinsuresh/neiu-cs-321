/*
* Domain class representing each image post in Doodle Board website.
* Created by: Sebin Puthenthara Suresh
* Created on: 09/09/2020
*
* Description of planned image/doodle implementation:
* ===================================================
* The creations, called doodles, are made up of 12 possible colors, as shown by the RGB hex code in PALETTE array.
* (The palette can be found at /src/main/resources/static/images/palette.png)
* The final color (#000000) in the array represents transparent pixels - However, right now they are shown as white
* color pixels instead.
* The doodles are also tiny, with dimensions of 64x64 pixels.
* The doodles will be constructed by passing in a String of hex values representing the index of the color.
* Thus, each 'data' String will have a length of 4096.
*
*
* Example of data String:
* ================
* A 10x10 heart image PNG file named heart.png can be found in the /src/main/resources/static/images/ folder.
* The string representation of that image would be:
* "BBBBBBBBBBBB00BB00BBB04200420B042224228002222222800222222280B02222280BBB022280BBBBB0280BBBBBBB00BBBB"
* Again, each of these characters represent one color from the palette at the index represented by the integer value of
* hex value of that character.
* Eg: '4' is the hex value 0x4, which is decimal 4. At index 4, it is the color #D7AF87
*     'B' on the other hand is index 11, which is #000000 -> which I'm using to stand in for a transparent pixel.
*
* */
package dboard;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Entity
public class DoodlePost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long postId;
    @NotEmpty(message = "The title cannot be empty")
    private String title;               // Title given to the doodle by the user.
    private LocalDateTime postedAt;     // Time at which the Doodle Post was made
    @Setter(AccessLevel.NONE)           // No setters for this variable, use the incrLikes() and decrLikes() instead.
    private int numLikes = 0;           // Number of likes on the doodle.

    @Valid
    @OneToOne
    private Doodle content;

    @ManyToOne
    private User user;

    public DoodlePost(){
        this.content = new Doodle();
    }

    // Method to increase the likes on the doodle by one.
    // Returns true if increment was successful, false otherwise.
    public boolean incrLikes(){
        if(this.numLikes < Integer.MAX_VALUE - 1){
            this.numLikes++;
            return true;
        }
        return false;
    }

    // Method to decrease the likes on the doodle by one.
    // Returns true if decrement was successful, false otherwise.
    public boolean decrLikes(){
        if(this.numLikes > 0){
            this.numLikes--;
            return true;
        }
        return false;
    }

    @PrePersist
    void postedAt(){
        this.postedAt = LocalDateTime.now();
    }

}
