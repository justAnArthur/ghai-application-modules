package fiit.vava.client.controllers._components.auth;

import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;

import java.io.IOException;

public class LogoutController {

    public void handleLogout() throws IOException {
        CredentialsManager.removeCredentials();

        Router.getInstance().removeSidebar();
        Router.getInstance().navigateTo("auth/login");
    }
}
