package response;
import resource.Resource;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class OkResponse extends Response {
  private boolean isHead;

  public OkResponse(Resource resource,boolean isHead) {
    super(resource);
    statusCode = 200;
    reasonPhrase = "OK";
    this.isHead = isHead;
  }

  @Override
  public void send(OutputStream outputStream) throws Exception {
    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(outputStream));
    output.write(this.statusLine());
    output.write(this.headerLine(this.server, this.nameServer));
    output.write(this.headerLine(this.date, this.getDate()));
    if (isHead) {
      this.setBody();
      output.write(resource.getMimeType());
      output.write(this.CRLF);
      output.write(this.headerLine("Content-Length", String.valueOf(this.getBody().length)));
      output.write(this.CRLF);
      output.flush();
      outputStream.write(this.getBody());
      outputStream.flush();
    }else{
      output.write(this.CRLF);
      output.flush();
    }
  }
}
