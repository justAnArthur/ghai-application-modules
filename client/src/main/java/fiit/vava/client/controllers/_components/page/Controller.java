package fiit.vava.client.controllers._components.page;

import javafx.scene.Node;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Pagination;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import fiit.vava.client.controllers._components.page.card.CardController;
import javafx.scene.layout.GridPane;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
public class Controller {

    @FXML
    private GridPane gridPane;

    private Node createCard(Object obj, String mode) {
      try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fiit/vava/client/fxml/_components/page/card/card.fxml"));
        Node card = loader.load();
        CardController controller = (CardController) loader.getController();
        controller.setObject(obj,mode);
        return card;
      } catch (IOException e){
          e.printStackTrace();
          return null;
      } 
    }

    public void populatePage(int pageIndex, String mode) {
        //TODO I need backend methods:
    // getDocumentTemplates(int skip, int amount)
    // getMyDocuments(int skip, int amount)
    // 
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        List<?> listOfAll = null; 

        switch (mode) {
          case "ClientTemplates":
          case "AdminTemplates":
            listOfAll = stub.getAllDocumentTemplates(Empty.newBuilder().build()).getTemplatesList();
            break;
          case "ClientDocuments":
            // listOfAll = stub.getMyDocuments(Empty.newBuilder().build(), /* skip*/ pageIndex*8, /*amount*/ 8).getTemplatesList();
            break;
          default:
            break;
        } 

        System.out.println(listOfAll.size());
        if (listOfAll.isEmpty()) {
            Label label = new Label("No templates found.");
            gridPane.add(label, 0, 0);
        } else {
            gridPane.getChildren().clear();
            int size = listOfAll.size();
            int limiter = 0;
            if(size - 8*pageIndex < 8){
              limiter = listOfAll.size();
            } else {
              limiter = 8*pageIndex + 8;
            }
            int rowIndex = 0, columnIndex = 0;
            for (int i = 8*pageIndex; i < limiter; i++) {
              Node card = createCard(listOfAll.get(i), mode);
              gridPane.add(card, columnIndex, rowIndex);
                if (++columnIndex > 3) {
                    columnIndex = 0;
                    rowIndex++;
                }
            }
        }
          // Spaghetti code above just waits for its methods. When methods are created uncomment code below, maybe correct some values, obviously code below hasn't been tested yet, but everything except for switch case clause should work fine.
        // List<Object> list = null;
        // switch (mode) {
        //   case "ClientTemplates":
        //   case "AdminTemplates":
        //     list = stub.getDocumentTemplates(Empty.newBuilder().build(), /* skip*/ pageIndex*8, /*amount*/ 8).getTemplatesList();
        //     break;
        //   case "ClientDocuments":
        //     list = stub.getMyDocuments(Empty.newBuilder().build(), /* skip*/ pageIndex*8, /*amount*/ 8).getTemplatesList();
        //   default:
        //     break;
        // } 
        // int rowIndex = 0, columnIndex = 0;
        // for(Object obj : list){
        //   VBox card = createCard(obj);
        //   gridPane.add(card, columnIndex, rowIndex);
        //     if (++columnIndex > 3) {
        //         columnIndex = 0;
        //         rowIndex++;
        //     }
        // }
    }
}
