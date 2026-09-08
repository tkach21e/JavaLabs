package Server.Modules;

import Common.Network.Response;
import Common.Network.Serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ResponseSender {

    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);

    public static void send(DatagramChannel channel,
                            Response response,
                            SocketAddress address) throws Exception {

        byte[] bytes = Serialization.serialize(response);
        // Один IPv4 UDP-пакет вмещает не более 65507 байт данных.
        if (bytes.length > 65507) {
            bytes = Serialization.serialize(new Response(
                    "Ответ слишком большой для одного UDP-пакета. Используйте фильтры.", false));
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        if (channel.send(buffer, address) == 0) {
            throw new IOException("UDP-пакет не отправлен: сетевой буфер занят.");
        }
        logger.debug("Ответ отправлен адресату: {}", address);
    }
}
