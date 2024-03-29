package fiit.vava.client.controllers._components.routing;

import fiit.vava.client.Router;

import java.io.IOException;

public class BackButtonController {
    public void handleButtonClick() throws IOException {
        Router.getInstance().navigateBack();
    }
}
