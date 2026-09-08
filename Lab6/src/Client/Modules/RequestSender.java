package Client.Modules;

import Common.Network.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class RequestSender {

    public static void sendRequest(String host,
                                   int port,
                                   Request request) {

        try (DatagramSocket socket = new DatagramSocket()) {

            socket.setSoTimeout(3000);

            byte[] requestBytes = Serialization.serialize(request);
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket requestPacket =
                    new DatagramPacket(
                            requestBytes,
                            requestBytes.length,
                            address,
                            port);

            socket.send(requestPacket);

            byte[] responseBuffer = new byte[65535];
            DatagramPacket responsePacket =
                    new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);

            byte[] responseBytes = new byte[responsePacket.getLength()];
            System.arraycopy(
                    responsePacket.getData(),
                    0,
                    responseBytes,
                    0,
                    responsePacket.getLength());

            Response response = (Response) Serialization.deserialize(responseBytes);
            System.out.println(response.getMessage());

        } catch (SocketTimeoutException e) {
            System.out.println("Сервер не отвечает.");
        } catch (Exception e) {
            System.out.println("Ошибка связи: " + e.getMessage());
        }
    }
}