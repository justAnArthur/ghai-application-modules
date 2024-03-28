# Changelog

#### justAnArthur on 3/28/2024 at 11:39 PM

### Client

- **Localization**
    - #### Added localization. Example in `auth/login.fxml` and `auth/LoginController.java`.

---

#### justAnArthur on 3/28/2024 at 10:54 PM

### Server

- **Repository**
    - #### Managed repository type for internal and sql implementation driving via `.env` file.

---

#### justAnArthur on 3/28/2024 at 9:04 PM

### Client

- **Router**
    - Refactored file-based routing to use a single `Router` class that handles all navigation.
    - With `Controller.java`s for every route.
    - #### Try to use shared components, as `routing.backButton` or `fileUpload`.
    - #### Removed static `Routes`. Just use a plain string.

- **Stubs Managed**
    - To call a server function -
      use `UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();`
      which automatically appends credentials and will manage additional things if required.

- **coworker/clients/approve**

### Server

- **UserService.java**
    - `approveClient`

---

#### <aldeimeter666@gmail.com> on 3/28/2024 at 6:29 AM

- **Router**
    - Router has rewritten to load another fxml without changing Scene. To make transitions between fxml files smoother.
      Router.getInstance().navigateTo(String path) to change center fxml file, e.g., page
      Router.getInstance().changeNavBar(String path) to change sidebar.
      Routes are written in Routes.java, maybe Hashmap was better, not sure yet.
      Init Hashmap and fill it with key-value Vs. write constant in .java file. Not sure what is better, as for me, it
      is more
      convenient to use constants
- **Client file structure**
    - Controllers for each role is now in its own package for easier navigation in the project structure.
      Same in resources, although now we have package "fiit.vava.client.client" in resources.
- **Testing purposes only**
    - There is a testing variable offline in LoginPageController.java, change it too false to enable server
      communication
      on the login screen.
