package fiit.vava.client.controllers.coworker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class UserVerificationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label sex;

    @FXML
    private Button approveBtn;

    @FXML
    private Label dateOfBirth;

    @FXML
    private Button disapproveBtn;

    @FXML
    private ImageView imageView;

    @FXML
    private Label name;

    @FXML
    private Label surname;

    @FXML
    private Label validFromDate;

    @FXML
    private Label validUntilDate;
    
    @FXML
    void initialize() {

    }
    
    @FXML
    private void handleApprove(){
      System.out.println("Action wasn't handled");
    }
    
    @FXML
    private void handleDisapprove(){
      System.out.println("Action wasn't handled");
    }

    @FXML 
    private void handleImageChange(){
      System.out.println("Action wasn't handled");
    }
    
    private void setData(String name, String surname, String dateOfBirth, String validFromDate, String validUntilDate, String sex) {
      this.name.setText(name);
      this.surname.setText(surname);
      this.dateOfBirth.setText(dateOfBirth);
      this.validFromDate.setText(validFromDate);
      this.validUntilDate.setText(validUntilDate);
      this.sex.setText(sex);
    }
}
