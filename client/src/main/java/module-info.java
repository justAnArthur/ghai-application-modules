module fitt.vava.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires io.grpc;

    requires server;
    requires annotations.api;
    requires jjwt;

    opens fiit.vava.client to javafx.fxml;
    exports fiit.vava.client;
    exports fiit.vava.client.controllers;
    opens fiit.vava.client.controllers to javafx.fxml;
}