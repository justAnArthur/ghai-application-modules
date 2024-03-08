module fitt.vava.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires io.grpc;

    opens fitt.vava.client to javafx.fxml;
    exports fitt.vava.client;
}