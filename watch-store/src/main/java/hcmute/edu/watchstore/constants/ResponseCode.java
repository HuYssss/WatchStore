package hcmute.edu.watchstore.constants;

public enum ResponseCode {
    OK(200, "Success"),
    NOT_FOUND(404, "Not found"),
    USER_NOT_FOUND(4040, "User not found"),
    PRODUCT_NOT_FOUND(4041, "Product not found"),
    NO_PARAM(6001, "No param"),
    INVALID_VALUE(6015, "Invalid value"),
    DUPLICATED_USERNAME(6016, "Duplicated username"),
    NO_CONTENT(2004, "No content"),
    INCORRECT_AUTHEN(2005, "Invalid username or password"),
    NOT_LOGIN(2006, "Not login"),
    CANNOT_SEND_EMAIL(5000, "Cannot send email"),
    EMAIL_ALREADY_REGISTERED(6017, "Email already registered"),
    ERROR_IN_PROCESSING(5001, "Error during execution");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}