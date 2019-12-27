package response;
import resource.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class NoContentResponse extends Response{
	public NoContentResponse(Resource resource) {
		super(resource);
		statusCode = 204;
		reasonPhrase = "No Content";
	}

	@Override
	public void send(OutputStream outputStream) throws Exception {
		File fileToDelete = new File(resource.getAbsolutePath());
		fileToDelete.delete();
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(outputStream));
		output.write(this.statusLine());
		output.write(this.headerLine(this.server,this.nameServer));
		output.write(this.headerLine(this.date,this.getDate()));
		output.write(this.CRLF);
		output.flush();
	}
}
