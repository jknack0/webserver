package configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MimeTypesConfiguration extends ConfigurationReader {
	final private String COMMENT_CHARACTER = "#";
	final private String DEFAULT_MIMETYPE = "text/text";
	private Map<String, String> mimeMap;

	public MimeTypesConfiguration(String fileName)	{
		super(new File(fileName));
		mimeMap = new HashMap();
	}

	public void load() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currentLine;
			while((currentLine = br.readLine()) != null){
				StringTokenizer st = new StringTokenizer(currentLine);
				if(currentLine.startsWith(COMMENT_CHARACTER))
					continue;
				while(st.hasMoreTokens()) {
					String mimeType = st.nextToken();
					while(st.hasMoreTokens()){
						String extension = st.nextToken();
						mimeMap.put(extension, mimeType);
					}
				}
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	public String lookup(String extension) {
		String mimetype;
		if(mimeMap.containsKey(extension)){
			mimetype = mimeMap.get(extension);
		} else {
			mimetype = DEFAULT_MIMETYPE;
		}
		return mimetype;
	}
}
