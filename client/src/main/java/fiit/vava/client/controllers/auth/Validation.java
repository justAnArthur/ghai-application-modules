package fiit.vava.client.controllers.auth;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class Validation {

    public static final Predicate<String> name = input -> input.matches("[A-Za-z]+");
    public static final Predicate<String> number = input -> input.matches("-?\\d*\\.?\\d*");
    public static final Predicate<String> email = input -> input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    public static final Predicate<String> password = input -> input.matches("^(?=.*\\d)[a-zA-Z\\d]{8}$");
    public static final Predicate<String> notNull = input -> input != null || !input.isEmpty();

    public static String validateFields(ValidationPair... validationPairs) {
        Optional<ValidationPair> errored = Arrays.stream(validationPairs)
                .filter(pair -> {
                    try {
                        return !pair.validate();
                    } catch (Exception e) {
                        return true;
                    }
                })
                .findFirst();

        if (errored.isPresent()) {
            return errored.get().message;
        }

        return null;
    }

    public static ValidationPair pair(String message, Predicate<String>... predicates) {
        return new ValidationPair(message, predicates);
    }

    public static class ValidationPair {
        private final Predicate<String>[] predicates;
        private final String message;
        private String value;

        public ValidationPair(String message, Predicate<String>... predicates) {
            this.predicates = predicates;
            this.message = message;
        }

        public boolean validate() {
            return Arrays.stream(predicates).allMatch(predicate -> predicate.test(this.value));
        }

        public ValidationPair set(String input) {
            this.value = input;
            return this;
        }
    }
}
