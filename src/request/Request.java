package request;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Request {
	private String uri;
	private byte[] body;
	private String verb;
	private String httpVersion;
	private HashMap<String, String> headers;
	private InputStream requestInputStream;
	private static final String CONTENT_LENGTH_HEADER = "Content-Length";
	private static final List<String> VALID_VERBS = Arrays.asList("GET", "POST", "HEAD", "PUT", "DELETE");
	private String requestLine;
	private boolean badRequest;

	public Request(InputStream requestInputStream){
		headers = new HashMap();
		this.requestInputStream = requestInputStream;
		badRequest = false;

	}

	public void parseHttpRequest () {
		try {
				BufferedReader requestBufferedReader = new BufferedReader(new InputStreamReader(requestInputStream));
				String line = requestBufferedReader.readLine();
				parseRequestLine(line);
				requestLine = line;
				line = requestBufferedReader.readLine();
				while (!line.isEmpty()) {
					parseRequestHeader(line);
					line = requestBufferedReader.readLine();
				}
				if (hasBody()) {
					parseBody(requestBufferedReader);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseBody(BufferedReader requestBufferedReader){
		try {
			StringBuilder bodyStringBuilder = new StringBuilder();
			int numberOfBytes = Integer.parseInt(headers.get(CONTENT_LENGTH_HEADER));
			int value = requestBufferedReader.read();
			for (int index = 0; index < numberOfBytes; index++) {
				char c = (char) value;
				bodyStringBuilder.append(c);
				if (index == numberOfBytes - 1) /// I hate this so much but I couldn't find a better way
					break;
				value = requestBufferedReader.read();
			}
			body = new byte[numberOfBytes];
			body = bodyStringBuilder.toString().getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasBody() {
		return headers.containsKey(CONTENT_LENGTH_HEADER);
	}

	public void parseRequestLine(String requestLine) {
		StringTokenizer requestLineToken = new StringTokenizer(requestLine);
		verb = requestLineToken.nextToken();
		if(!(isValidVerb(verb))){
			badRequest = true;
		}
		uri = requestLineToken.nextToken();
		httpVersion = requestLineToken.nextToken();
	}

	public void parseRequestHeader(String headerLine) {
		String headerName;
		String headerValue;
		StringTokenizer headerLineTokenizer = new StringTokenizer(headerLine, ": ");
		headerName = headerLineTokenizer.nextToken();
		headerValue = headerLineTokenizer.nextToken();
		headers.put(headerName, headerValue);
	}

	public String getUri() {
		return uri;
	}

	public String getVerb() {
		return this.verb;
	}

	public Boolean isValidVerb(String verb) {
		return VALID_VERBS.contains(verb);
	}

	public byte[] getBody() {
		return body;
	}

	public String getRequestLine(){
		return " \"" + requestLine + "\" ";
	}

	public String getRequestIPAdress(){
		return headers.get("Host");
	}

	public boolean isBadRequest() {
		return badRequest;
	}
}
