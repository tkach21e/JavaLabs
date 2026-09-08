package Server.Modules;

import Common.Network.Request;

import java.net.SocketAddress;

public class RequestData {

    private final Request request;
    private final SocketAddress clientAddress;

    public RequestData(Request request, SocketAddress clientAddress) {
        this.request = request;
        this.clientAddress = clientAddress;
    }

    public Request getRequest() {
        return request;
    }

    public SocketAddress getClientAddress() {
        return clientAddress;
    }
}