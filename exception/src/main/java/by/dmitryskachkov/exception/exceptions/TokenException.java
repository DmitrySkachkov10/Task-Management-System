package by.dmitryskachkov.exception.exceptions;

import org.springframework.http.HttpStatusCode;

public class TokenException extends RuntimeException {

    private HttpStatusCode httpStatusCode;

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

