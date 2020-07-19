package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Token;
import org.springframework.http.HttpMethod;

public abstract class SecurityService<T extends Entity> {

    private Token token;
    private HttpMethod httpMethod;

    public abstract T anonymiseEntity(T entity);

    public abstract boolean isLegalEntityOperation(T entity);

    public Token getToken() {
        return token;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
