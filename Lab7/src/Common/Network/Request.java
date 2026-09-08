package Common.Network;

import Common.Model.Product;
import java.io.Serializable;

public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String commandName;
    private Product product;
    private Long id;
    private String stringArgument;
    private String login;
    private String password;

    public Request(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringArgument() {
        return stringArgument;
    }

    public void setStringArgument(String stringArgument) {
        this.stringArgument = stringArgument;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
