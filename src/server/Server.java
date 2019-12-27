package server;

import configuration.HttpdConfiguration;
import configuration.MimeTypesConfiguration;
import worker.Worker;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  private static final String httpdFilePath = "conf/httpd.conf";
  private static final String mimeFilePath = "conf/mime.types";
  private static HttpdConfiguration configuration;
  private static MimeTypesConfiguration mimeType;
  private ServerSocket serverSocket;
  private Socket client;

  public Server() throws Exception {
    configuration = new HttpdConfiguration(httpdFilePath);
    mimeType = new MimeTypesConfiguration(mimeFilePath);
    configuration.load();
    mimeType.load();
    serverSocket = null;
    client = null;
  }

  public void start() throws Exception {
    serverSocket = new ServerSocket(configuration.getListenPort());
    System.out.println("Server started");
    while (true) {
      client = serverSocket.accept();
      Worker worker = new Worker(client, configuration, mimeType);
      worker.start();
    }
  }
}
