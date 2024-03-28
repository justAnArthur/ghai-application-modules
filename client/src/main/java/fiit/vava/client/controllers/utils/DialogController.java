package fiit.vava.client.controllers.utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import fiit.vava.client.Router;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
public class DialogController{
    
    private File frontSideFile;
    private File backSideFile;
    @FXML 
    private Button frontSideBtn;
    
    @FXML 
    private Button backSideBtn;
    
    @FXML
    private Label backSideLabel;

    @FXML
    private Label frontSideLabel;

    @FXML
    private ImageView frontSideImageView;

    @FXML
    private ImageView backSideImageView;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {
      backSideBtn.setVisible(false);
      backSideImageView.setVisible(false);
      backSideLabel.setVisible(false);
      frontSideImageView.setVisible(false);
    }
    @FXML
    private void toggleImageSide() {
      boolean toggle = frontSideLabel.isVisible();
      frontSideLabel.setVisible(!toggle);
      frontSideImageView.setVisible(!toggle);
      backSideLabel.setVisible(toggle);
      backSideImageView.setVisible(toggle);
    }
    @FXML
    private void handleDocumentFrontSide(){
      FileChooser fileChooser = new FileChooser();
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Image Files", "*.*");
      fileChooser.getExtensionFilters().add(extFilter);
      frontSideFile = fileChooser.showOpenDialog(frontSideBtn.getScene().getWindow());
      
      if (frontSideFile != null){
        System.out.println("Selected File:" + frontSideFile.getAbsolutePath());
      
        frontSideBtn.setVisible(false);
        toggleImageSide();
        backSideBtn.setVisible(true);

        Image image = new Image(frontSideFile.toURI().toString());
        frontSideImageView.setImage(image);
      } else {
        System.out.println("No file choosen");
      }
    }
    @FXML
    private void handleDocumentBackSide(){
      FileChooser fileChooser = new FileChooser();
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Image Files", "*.*");
      fileChooser.getExtensionFilters().add(extFilter);
      backSideFile = fileChooser.showOpenDialog(backSideBtn.getScene().getWindow());
      
      if (backSideFile != null){
        System.out.println("Selected File:" + backSideFile.getAbsolutePath());
        backSideBtn.setVisible(false);
        Image image = new Image(backSideFile.toURI().toString());
        backSideImageView.setImage(image);
      } else {
        System.out.println("No file choosen");
      }
    }

    @FXML
    private void saveImages() {
        if (backSideFile == null || frontSideFile == null) {
            System.err.println("Error: No image files selected.");
            return;
        }

        String backName = generateUniqueName(backSideFile.getName());
        String backExt = getFileExtension(backSideFile.getName());
        String frontName = generateUniqueName(frontSideFile.getName());
        String frontExt = getFileExtension(frontSideFile.getName());

        if (backName != null && backExt != null && frontName != null && frontExt != null) {
            System.out.println(System.getProperty("user.dir"));
            String path = System.getProperty("user.dir");
            String backDestination = path + "/" + backName + "." + backExt;
            String frontDestination = path + "/" + frontName + "." + frontExt;

            try {
                saveImage(backSideFile, backDestination);
                saveImage(frontSideFile, frontDestination);
                System.out.println("Images saved successfully.");
                Router.getInstance().hideModal();
            } catch (IOException e) {
                System.err.println("Failed to save images: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Error: Unable to determine image name or extension.");
        }
    }
    // put save to backend logic here if possible. 
    private void saveImage(File source, String destinationPath) throws IOException{
      // Copy the image file to the destination path
      Path destination = Path.of(destinationPath);
      System.out.println(destination);
      Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
      System.out.println("Image saved successfully to: " + destination);
    }

    @FXML
    private void cancel(){
      Router.getInstance().hideModal();
    }

    private String generateUniqueName(String imageName) {
        // Get the current timestamp in milliseconds
        long timestamp = Instant.now().toEpochMilli();

        // Concatenate the image name with the timestamp
        String combinedString = imageName + "_" + timestamp;

        // Hash the combined string using SHA-256
        String uniqueName = sha256Hash(combinedString);

        return uniqueName;
    }

    private String sha256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileExtension(String fileName) {
        // Find the index of the last dot character
        int dotIndex = fileName.lastIndexOf('.');

        // Check if a dot was found and it's not the last character
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            // Extract the substring after the dot
            return fileName.substring(dotIndex + 1);
        } else {
            // No extension found or dot is the last character
            return null;
        }
    }
}
