# Changelog 
## Router
Router rewritten to load another fxml without changing Scene. To make transitions between fxml files more smooth.
Router.getInstance().navigateTo(String path) to change center fxml file, e.g. page
Router.getInstance().changeNavBar(String path) to change side-bar.
Routes are written in Routes.java, maybe Hashmap was better, not sure yet.
init Hashmap and fill it with key-value Vs. write constant in .java file. Not sure what is better, as for me, it is more convenient to use constants

## Client file structure

Controllers for each role is now in its own package for easier navigation in project structure.
Same in resources, eventhough now we have package "fiit.vava.client.client" in resources.

## Testing purposes only
There is testing variable offline in LoginPageController.java, change it to false to enable server communication on login screen.
