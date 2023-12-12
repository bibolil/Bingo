package org.bingo.rpc;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;


public class BingoServer {
    public static void main(String[] args) {
        try {
            WebServer webServer = new WebServer(5002);

            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
            XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
            serverConfig.setEnabledForExtensions(true);
            serverConfig.setContentLengthOptional(false);
            PropertyHandlerMapping phm = new PropertyHandlerMapping();
            phm.addHandler("Bingo", Bingo.class);
            xmlRpcServer.setHandlerMapping(phm);

            webServer.start();
            System.out.println("Server started");
        } catch (Exception e) {
            System.err.println("Error: " + e);
            e.printStackTrace();
        }
    }
}
