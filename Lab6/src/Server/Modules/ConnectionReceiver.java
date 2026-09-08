package Server.Modules;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class ConnectionReceiver {

    private final DatagramChannel channel;
    private final int port;

    public ConnectionReceiver(int port) throws IOException {
        this.port = port;
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
    }

    public DatagramChannel getChannel() {
        return channel;
    }

    public int getPort() {
        return port;
    }
}