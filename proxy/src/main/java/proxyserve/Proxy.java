package proxyserve;

public class Proxy {

    public static void main(String[] args) {
        Router router = new Router();
        ProxyServer proxyServer = new ProxyServer();
        proxyServer.injectRouter(router);
        proxyServer.listen();
    }
}
