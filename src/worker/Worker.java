package worker;
import configuration.HttpdConfiguration;
import configuration.MimeTypesConfiguration;
import logger.Logger;
import request.Request;
import resource.Resource;
import response.Response;
import response.ResponseFactory;
import java.io.File;
import java.net.Socket;

public class Worker extends Thread{
  private Socket client;
  private HttpdConfiguration configuration;
  private MimeTypesConfiguration mimeType;
  private Request request;
  private Resource resource;
  private Response response;
  private Logger logger;

  public Worker(Socket client, HttpdConfiguration configuration, MimeTypesConfiguration mimeType) {
    this.client = client;
    this.configuration = configuration;
    this.mimeType = mimeType;
  }

  public void run(){
    try {
      request = new Request(client.getInputStream());
      request.parseHttpRequest();
      resource = new Resource(request.getBody(),request.getUri(),configuration,mimeType);
      resource.resolve();
      ResponseFactory responseFactory = new ResponseFactory();
      if(request.isBadRequest()){
        responseFactory.setBadRequest(true);
      }
      response = responseFactory.getResponse(request,resource);
      response.send(client.getOutputStream());
      client.close();
      logger = new Logger(new File(configuration.getLogFile()), request, response);
      logger.write();
    }catch (Exception e){
    }
  }
}
