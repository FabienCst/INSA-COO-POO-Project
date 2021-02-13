package bavard.network;

import bavard.chat.ChatService;

public class ProxyConnection {
    
    private NetworkService networkService;
    private ChatService chatService;
    
    public void injectNetworkService(NetworkService networkService) { this.networkService = networkService; }
    public void injectChatService(ChatService chatService) { this.chatService = chatService; }

    public void connect() {
    }
}
