package fiit.vava.client.controllers.admin.templates;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.Empty;
import fiit.vava.server.GetFileByPathRequest;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import fiit.vava.client.controllers._components.page.Controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class TemplatesController {
    private static final int ITEMS_PER_PAGE = 8;
    @FXML 
    BorderPane borderPane;
    @FXML 
    Pagination pagination; 
    /*
     * TODO import VBox from `.fxml` file:
     * ```java
     * FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/your/card.fxml"));
     * // Create the VBox
     * VBox card = loader.load();
     * ```
     */
    @FXML
    void initialize() {
        init_pagination();
    }
    private void init_pagination(){
      //TODO I need getAmountOfDocuments and getAmountOfDocumentTemplates for pagination 
      DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();
      // int amountOfElements = stub.getAmountOfDocuments();
      int amountOfElements = stub.getAllDocumentTemplates(Empty.newBuilder().build()).getTemplatesList().size();
      int pages = 0;
      if(amountOfElements < ITEMS_PER_PAGE){
        pages = 1;
      }else{
       pages = (amountOfElements - (amountOfElements % ITEMS_PER_PAGE)) / ITEMS_PER_PAGE;
      }
      pagination.setPageCount(pages);
      // pagination.setPageFactory(this::createPage);
      
      pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            // Load the new content into, for example, the center of the BorderPane
            borderPane.setCenter(createPage(newIndex.intValue()));
        });

        // Initially load the first page content into the center
        borderPane.setCenter(createPage(0));
    }
    
    private Node createPage(int pageIndex){
      try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fiit/vava/client/fxml/_components/page/paginationGrid.fxml"));
        Node pageContent = loader.load();
        Controller controller = (Controller) loader.getController();
        controller.populatePage(pageIndex, "AdminTemplates");
        return pageContent;
      } catch (IOException e){
          e.printStackTrace();
          return null;
      }
    }
    @FXML
    public void handleCreateAction(ActionEvent actionEvent) throws IOException {
        Router.getInstance().navigateTo("admin/templates/create");
    }
}
