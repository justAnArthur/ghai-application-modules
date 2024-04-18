package fiit.vava.client.controllers.auth;

import java.util.function.Predicate;
import javafx.scene.control.TextField;
public class Validation {
  public static final Predicate<String> emailValidation = input -> input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"); 

  public static final Predicate<String> passwordValidation = input -> input.matches("^(?=.*\\d)[a-zA-Z\\d]{8}$");
  
  public static final Predicate<String> nameValidation = input -> input.matches("[A-Za-z]+");
  public static void validateField(String input, TextField field, Predicate<String> validationFunction) {
        if(input.equals("")){
          field.setStyle(null);
        }
        else if(validationFunction.test(input)){
          field.setStyle("-fx-border-color: green;");
        }else {
          field.setStyle("-fx-border-color: red;");
        }
    }
}
