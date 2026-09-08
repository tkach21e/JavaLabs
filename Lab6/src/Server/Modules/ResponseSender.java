package Server.Modules;

import Common.Network.Response;
import Common.Network.Serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ResponseSender {

    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);

    public static void send(DatagramChannel channel,
                            Response response,
                            SocketAddress address) throws Exception {

        byte[] bytes = Serialization.serialize(response);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        channel.send(buffer, address);
        logger.debug("Ответ отправлен адресату: {}", address);
    }
}