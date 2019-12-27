package response;
import resource.Resource;
import java.io.*;

public class CreatedResponse extends Response {
	public CreatedResponse(Resource resource) {
		super(resource);
		statusCode = 201;
		reasonPhrase = "Created";
	}

	@Override
	public void send(OutputStream outputStream) throws Exception {
		File fileToWriteTo = new File(resource.getAbsolutePath());
		OutputStream fileOutputStream = new FileOutputStream(fileToWriteTo);
		fileOutputStream.write(resource.getBody());
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(outputStream));
		output.write(this.statusLine());
		output.write(this.headerLine(this.server,this.nameServer));
		output.write(this.headerLine(this.date,this.getDate()));
		output.write(this.CRLF);
		output.flush();
	}


}

