package bavard.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import bavard.chat.ChatService;
import bavard.chat.Message;
import bavard.chat.TextMessage;
import bavard.user.UserService;
import javafx.application.Platform;

public class ProxyConnection {
    
    private NetworkService networkService;
    private ChatService chatService;
    private UserService userService;

    private Socket commandConnection;
    private Socket messageConnection;
    
    public Socket getCommandConnection() { return commandConnection; }
    public Socket getMessageConnection() { return messageConnection; }

    public void connect() {
        try {
            Socket comConnection = new Socket(InetAddress.getLoopbackAddress(), 7777);
            commandConnection = comConnection;
            
            new Thread(() -> {
                try {
                    InputStream is = commandConnection.getInputStream();
                    byte[] receivedEventBytes = new byte[65536];
        
                    while (true) {
                            
                        if (is.read(receivedEventBytes) > 0) {
                            NetworkEvent networkEvent = NetworkEvent.deserialize(receivedEventBytes);
        
                            Platform.runLater(() -> {
                                networkService.handleNetworkEvent(networkEvent);
                            });
                        }
                    }
                } catch (IOException ioe) {
                    // Oops, couldn't open socket stream
                } catch (ClassNotFoundException deserializeException) {
                    // Ignore this packet and wait for another
                }
            }).start();
            
            Socket mesConnection = new Socket(InetAddress.getLoopbackAddress(), 7766);
            messageConnection = mesConnection;

            Message dummy = new TextMessage(userService.getCurrentUser(), null, null, null);
            OutputStream dummyOutputStream = messageConnection.getOutputStream();
            dummyOutputStream.write(Message.serialize(dummy));
            dummyOutputStream.flush();

            new Thread(() -> {
                try {
                    InputStream is = messageConnection.getInputStream();
                    byte[] receivedMessagetBytes = new byte[65536];

                    while (true) {
                    
                        if (is.read(receivedMessagetBytes) > 0) {
                            Message message = Message.deserialize(receivedMessagetBytes);

                            chatService.receiveMessage(message);
                        }
                    }
                } catch (IOException ioe) {
                    // Oops, couldn't open socket stream
                } catch (ClassNotFoundException deserializeException) {
                    // Ignore this packet and wait for another
                }
            }).start();            
        } catch (IOException e) {
            // Oops, could not open a socket
        } 
    }

    public void injectNetworkService(NetworkService networkService) { this.networkService = networkService; }
    public void injectChatService(ChatService chatService) { this.chatService = chatService; }
    public void injectUserService(UserService userService) { this.userService = userService; }
}
