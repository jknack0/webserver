package response;
import resource.Resource;
import java.io.*;

public class BadRequestResponse extends Response {
  private byte[] uniqueBody;

  public BadRequestResponse(Resource resource) {
    super(resource);
    this.statusCode = 400;
    this.reasonPhrase = "Bad Request";
  }

  @Override
  public void send(OutputStream outputStream) throws Exception {
    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(outputStream));
    setUniqueBody();
    output.write(this.statusLine());
    output.write(this.headerLine(this.server,this.nameServer));
    output.write(this.headerLine(this.date,this.getDate()));
    output.write("");
    output.write("Content-Type: text/html");
    output.write(this.CRLF);
    output.write(this.headerLine("Content-Length",String.valueOf(this.uniqueBody.length)));
    output.write(this.CRLF);
    output.flush();
    outputStream.write(uniqueBody);
    outputStream.flush();
  }

  private void setUniqueBody(){
    try{
      File file = new File("response/htmlResponse/badRequest.html");
      uniqueBody = new byte[(int)file.length()];
      InputStream is = new FileInputStream(file);
      is.read(uniqueBody);
      is.close();
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
