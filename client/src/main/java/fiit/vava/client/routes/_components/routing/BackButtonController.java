package fiit.vava.client.routes._components.routing;

import fiit.vava.client.Router;

import java.io.IOException;

public class BackButtonController {
    public void handleButtonClick() throws IOException {
        Router.getInstance().navigateBack();
    }
}
