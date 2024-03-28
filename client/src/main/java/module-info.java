module fitt.vava.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires io.grpc;

    requires server;
    requires annotations.api;
    requires jjwt;
    requires io.grpc.stub;
    requires org.slf4j;
    requires MaterialFX;

    opens fiit.vava.client to javafx.fxml;
    exports fiit.vava.client;

    opens fiit.vava.client.routes to javafx.fxml;

    exports fiit.vava.client.routes.admin;
    opens fiit.vava.client.routes.admin to javafx.fxml;

    exports fiit.vava.client.routes.client;
    opens fiit.vava.client.routes.client to javafx.fxml;

    exports fiit.vava.client.routes.coworker;
    opens fiit.vava.client.routes.coworker to javafx.fxml;

    exports fiit.vava.client.routes.coworker.clients.approve;
    opens fiit.vava.client.routes.coworker.clients.approve to javafx.fxml;

    exports fiit.vava.client.routes._components.routing;
    opens fiit.vava.client.routes._components.routing to javafx.fxml;

    exports fiit.vava.client.routes.auth.login;
    opens fiit.vava.client.routes.auth.login to javafx.fxml;

    exports fiit.vava.client.routes.client.documents;
    opens fiit.vava.client.routes.client.documents to javafx.fxml;

    exports fiit.vava.client.routes.client.profile;
    opens fiit.vava.client.routes.client.profile to javafx.fxml;
}