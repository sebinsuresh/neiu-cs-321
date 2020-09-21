/*
* Domain class representing each image post in Doodle Board website.
* Created by: Sebin Puthenthara Suresh
* Created on: 09/09/2020
*
* Description of planned image/doodle implementation:
* ===================================================
* The creations, called doodles, are made up of 12 possible colors, as shown by the RGB hex code in PALETTE array.
* (The palette can be found at /src/main/resources/static/images/palette.png)
* The final color (#000000) in the array represents transparent pixels, as there is capability to produce
* transparency-enabled PNG files.
* The doodles are also tiny, with dimensions of 64x64 pixels.
* The doodles will be constructed by passing in a String of hex values representing the index of the color.
* Values greater than 0xB (hex B = decimal 11) will be made to be 0xB (color #000000/transparent).
* Thus, each 'data' String will have a length of 4096.
*
*
* Parameters:
* ===========
* Each user-generated doodle on the website has a unique integer ID, a String title given to the doodle by the user,
* the unique int ID of the user who created it, an int numLikes keeping track of the number of likes on the doodle,
* and the data String itself for the doodle image content.
*
* Notes:
* ======
* Alternate implementation of the data could be done using a BigInteger or int[][] arrays.
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

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class DoodlePost {

    //private int postId;           // ID of the doodle post.
    @NotEmpty(message = "The title cannot be empty")
    private String title;         // Title given to the doodle by the user.
    //private int userId;           // ID of the user who posted it.

    @Setter(AccessLevel.NONE)           // No setters for this variable, use the incrLikes() and decrLikes() instead.
    private int numLikes = 0;           // Number of likes on the doodle.

    @Valid
    private Doodle content;

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
        if(this.numLikes > 1){
            this.numLikes--;
            return true;
        }
        return false;
    }

}
