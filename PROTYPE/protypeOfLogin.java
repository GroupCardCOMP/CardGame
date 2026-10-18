import java.io.File;




public class protypeOfLogin {



    public static void main(String[] args) {


        /* 
         this code creates a user directory if it doesn't already exist.
         this ensures that the necessary directory structure is in place before any user data is stored.
        
        
        
        */
    
        String userHome = System.getProperty("user.home");

        File folder = new File(userHome + "/CardGameUsers");
        if (folder.exists()) {
            System.out.println("User directory already exists.");
        } else {
            folder.mkdir();
        }


        System.out.print("Enter your username: ");
        
        


    }
}