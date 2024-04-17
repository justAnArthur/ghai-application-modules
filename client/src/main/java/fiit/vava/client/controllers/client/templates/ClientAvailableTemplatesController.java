package fiit.vava.client.controllers.client.templates;


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


public class ClientAvailableTemplatesController {

    private static final int ITEMS_PER_PAGE = 8;
    @FXML 
    BorderPane borderPane;
    @FXML 
    Pagination pagination; 
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
      
      pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
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
        controller.populatePage(pageIndex, "ClientTemplates");
        return pageContent;
      } catch (IOException e){
          e.printStackTrace();
          return null;
      }
    }
    // @FXML
    // public GridPane gridPane;
    //
    // /*
    //  * Load cards from .fxml with styles
    //  */
    // private Node createTemplateCard(DocumentTemplate template) {
    //     VBox card = new VBox() {{
    //         setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
    //     }};
    //
    //     card.setOnMouseClicked(event -> {
    //         try {
    //             Router.getInstance().navigateTo("client/templates/createBy/" + template.getId());
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     });
    //
    //     Label namelabel = new Label(template.getName());
    //     Label idlabel = new Label(template.getId());
    //
    //     card.getChildren().addAll(namelabel, idlabel);
    //
    //     return card;
    // }
    //
    // @FXML
    // public void initialize() {
    //     loadTemplates();
    // }
    //
    // private void loadTemplates() {
    //     DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();
    //
    //     stub.getAllDocumentTemplates(Empty.newBuilder().build())
    //             .getTemplatesList()
    //             .forEach(template -> {
    //                 System.out.println("document template: " + template.getId());
    //                 gridPane.getChildren().add(createTemplateCard(template));
    //             });
    // }
}
