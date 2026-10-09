import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class protypeOfLogin {



    public static void main(String[] args) {


        /* 
         this code creates a user directory if it doesn't already exist.
         this ensures that the necessary directory structure is in place before any user data is stored.
        
        
        
        */
    
        String userHome = System.getProperty("user.home");

        try {
            Path dir = Files.createDirectories(Paths.get(userHome, "CardGame/User"));
            System.out.println("User directory is ready.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();    
            System.out.println("Failed to create user directory.");
        }


        System.out.print("Enter your username: ");
        
        


    }
}