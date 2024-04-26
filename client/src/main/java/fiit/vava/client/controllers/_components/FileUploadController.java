package fiit.vava.client.controllers._components;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.Router;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.function.Function;

public class FileUploadController {

    public static final String FILE_SHARED_KEY = "uploadedPhoto";
    public static final String FILE_SHARED_CONTROLLER = "uploadingController";

    public Label labelCoverPhoto;
    public ImageView coverPhotoPlaceholder;
    public Button uploadButton;
    public Label fileFormatInfo;


    XMLResourceBundleProvider instance;

    public FileUploadController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }
    public String[] allowedFileFormats = new String[]{"*.jpg", "*.jpeg", "*.png"};

    private Function<File, Image> fromFileToImage = selectedFile -> new Image(selectedFile.toURI().toString());

    public void handleUpload() {
        FileChooser fileChooser = new FileChooser();

        String userHome = System.getProperty("user.home");
        File downloadDir = new File(userHome + "/Downloads");

        if (downloadDir.exists())
            fileChooser.setInitialDirectory(downloadDir);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(fileFormatInfo.getText(), allowedFileFormats);
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());

        if (selectedFile == null)
            return;

        Router.getInstance().addSharedData(FILE_SHARED_KEY, selectedFile);

        labelCoverPhoto.setText(selectedFile.getName());

        Image image = fromFileToImage.apply(selectedFile);
        coverPhotoPlaceholder.setImage(image);
    }

    public void initialize() {
        loadTexts();

        instance.subscribe(language -> loadTexts());
        Router.getInstance().addSharedData(FILE_SHARED_CONTROLLER, this);
    }

    public void setAllowedFileFormats(String[] allowedFileFormats) {
        this.allowedFileFormats = allowedFileFormats;
    }

    public ImageView getCoverPhotoPlaceholder() {
        return coverPhotoPlaceholder;
    }

    public Label getFileFormatInfo() {
        return fileFormatInfo;
    }

    public void setFromFileToImage(Function<File, Image> fromFileToImage) {
        this.fromFileToImage = fromFileToImage;
    }
    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.components");

        if (bundle == null)
            return;
        uploadButton.setText(bundle.getString("upload.button.upload")); 
        labelCoverPhoto.setText(bundle.getString("upload.label.label")); 
    }
}
