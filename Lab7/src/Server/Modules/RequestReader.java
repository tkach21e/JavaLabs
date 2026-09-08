package Server.Modules;

import Common.Network.Request;
import Common.Network.Serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class RequestReader {

    private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);

    public static RequestData read(DatagramChannel channel) throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(65535);
        SocketAddress address = channel.receive(buffer);
        if (address == null) return null;
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        Request request = (Request) Serialization.deserialize(bytes);
        logger.debug("Запрос получен, команда: {}", request.getCommandName());
        return new RequestData(request, address);
    }
}
