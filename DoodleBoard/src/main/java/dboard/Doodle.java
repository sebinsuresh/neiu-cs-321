package dboard;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Data
@Slf4j
@Entity
public class Doodle {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long DoodleId;

    @NotNull(message="The doodle string can't be empty")
    @Pattern(regexp="^(?!b{4096}$)([a-f0-9 ]+)$", message = "The doodle can't be empty (draw something)")   // accepts everythng except 'b' 4096 times (original empty string)
    @Size(min = 4096, max = 4096, message = "The doodle has to be 64x64")
    private String data;                // String representing the pixel by pixel value of the doodle.
    // private final int[][] data;      // int array representing the pixel by pixel value of the doodle.

    // Constructor
    public Doodle(){
        this.readEmptyData();
    }

    // Method that reads empty data string from text file to initialize this.data
    private void readEmptyData(){
        try {
            Scanner s = new Scanner(new File("./src/main/resources/static/data/empty_drawing.txt"));
            this.data = s.nextLine();
            s.close();
            // log.info("Empty drawing file found.");

        } catch(FileNotFoundException ex){
            log.info("Empty drawing file not found, populating manually");
            this.data = "B";
            for(int i = 0; i < 12; i++){
                this.data += this.data; // String of 4096 length at the end
            }
        } catch (Exception ex){
            log.error("Other exception.\n" + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
