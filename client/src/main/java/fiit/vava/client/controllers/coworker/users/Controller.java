package fiit.vava.client.controllers.coworker.users;

import fiit.vava.client.StubsManager;
import fiit.vava.server.Client;
import fiit.vava.server.CoworkerServiceGrpc;
import fiit.vava.server.Empty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    private static final Logger logger = LoggerFactory.getLogger("client.coworker.users" + Controller.class);

    @FXML
    void initialize() {
        loadData();
    }

    @FXML
    private void handleApprove() {
        CoworkerServiceGrpc.CoworkerServiceBlockingStub stub = StubsManager.getInstance().getCoworkerServiceBlockingStub();

        stub.approveClient(client);
        loadData();
    }

    @FXML
    private void handleDisapprove() {
        //TODO Handle disapprove, idk what should be done there
    }

    @FXML
    private void handleImageChange() {
        int imageId = toggleImage ? 1 : 0;
        toggleImage = !toggleImage;
        logger.debug(imageId + " ");
        imageView.setImage(images.get(imageId));
    }

    private void loadData() {
        CoworkerServiceGrpc.CoworkerServiceBlockingStub stub = StubsManager.getInstance().getCoworkerServiceBlockingStub();
        try {
            client = stub.getNonApprovedClients(Empty.newBuilder().build()).getClientList().get(0);
            // TODO insert method to retrieve passport images here.

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
