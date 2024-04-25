package fiit.vava.client.controllers.admin;

import fiit.vava.client.Router;

import java.io.IOException;

public class AdminController {

    public void initialize() throws IOException {
        Router.getInstance().setSideBar("admin/sideBar");
    }
}
