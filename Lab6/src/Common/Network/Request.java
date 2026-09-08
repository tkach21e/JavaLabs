package Common.Network;

import Common.Model.Product;
import java.io.Serializable;

public class Request implements Serializable {

    private final String commandName;
    private Product product;
    private Long id;
    private String stringArgument;

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
}