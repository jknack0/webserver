package response;

import resource.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Response {

  protected Resource resource;
  protected int statusCode;
  protected String reasonPhrase;
  protected String absolutePath;
  protected final String CRLF = "\r\n";
  protected final String httpVersion = "HTTP/1.1";
  protected final String server = "Server";
  protected final String date = "Date";
  protected final String nameServer = "Jon-Amir";
  protected byte[] body;
  protected int bodyLength;
  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

  public Response(Resource resource) {
    try {
      this.resource = resource;
      absolutePath = resource.getAbsolutePath();


    }catch (Exception e){}
    }

  public abstract void send(OutputStream outputStream) throws Exception;

  protected String statusLine(){
    return (httpVersion + ' ' + statusCode + " " + reasonPhrase + CRLF);
  }

  protected String headerLine(String type, String value){
        return (type + ": " + value + CRLF);
  }

  protected void setBody(){
    try{
      File file = new File(absolutePath);
      body = new byte[(int)file.length()];
      bodyLength = body.length;
      InputStream is = new FileInputStream(file);
      is.read(body);
      is.close();
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

  protected byte[] getBody(){
    return body;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public String getDate(){
    Date date = new Date();
    String currenDate = simpleDateFormat.format(date);
    return currenDate;
  }

  public int getBodyLength() {
    return bodyLength;
  }
}
