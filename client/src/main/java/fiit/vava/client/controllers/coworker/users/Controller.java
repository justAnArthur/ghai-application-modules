package fiit.vava.client.controllers.coworker.users;

import fiit.vava.client.StubsManager;
import fiit.vava.server.Client;
import fiit.vava.server.Empty;
import fiit.vava.server.UserServiceGrpc;
import java.net.URL;
import java.lang.IndexOutOfBoundsException;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Controller {
    
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button approveBtn;

    @FXML
    private Label dateOfBirth;

    @FXML
    private Label lowerLabel;

    @FXML
    private Button disapproveBtn;

    @FXML
    private ImageView imageView;

    @FXML
    private Label firstname;

    @FXML
    private Label lastname;

    @FXML
    private Label email;

    @FXML
    private Label validUntilDate;
    
    private Client client;
    
    private boolean toggleImage = false;
    
    private List<Image> images;
    @FXML
    void initialize() {
      loadData();
    }
    
    @FXML
    private void handleApprove() {
        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

        stub.approveClient(client);
        loadData();
    }
    
    @FXML
    private void handleDisapprove(){
      //TODO Handle disapprove, idk what should be done there 
    }

    @FXML 
    private void handleImageChange(){
        int imageId = toggleImage ? 1 : 0;
        toggleImage = !toggleImage;
        System.out.println(imageId);
        imageView.setImage(images.get(imageId));
    }
    
    private void loadData(){
        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();
        try {
            client = stub.getNonApprovedClients(Empty.newBuilder().build()).getClientList().get(0);
            // TODO insert method to retrive passport images here. 
            
            // uncomment when images are present
            // handleImageChange(); 
            lowerLabel.setText("Click to see another side of document"); 
            this.firstname.setText(client.getFirstName());
            this.lastname.setText(client.getLastName());
            this.dateOfBirth.setText("");
            this.email.setText(client.getUser().getEmail());
        } catch (IndexOutOfBoundsException e) {
            this.firstname.setText("");
            this.dateOfBirth.setText("");
            this.lastname.setText("");
            this.email.setText("");
            lowerLabel.setText("No clients to approve"); 
        }
    }
    
}
