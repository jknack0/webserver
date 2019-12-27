package resource;

import configuration.HttpdConfiguration;
import configuration.MimeTypesConfiguration;

public class Resource {
	private String absolutePath;
	private String[] splitURI;
	private String modifiedURI;
	private String URI;
	private HttpdConfiguration httpdConfiguration;
	private MimeTypesConfiguration mimeTypesConfiguration;
	private boolean isScript;
	private String mimeType;
	private byte[] body;

	public Resource (byte[] body, String uri, HttpdConfiguration httpdConfiguration, MimeTypesConfiguration mimeTypesConfiguration){
		this.body = body;
		this.URI = uri;
		this.httpdConfiguration = httpdConfiguration;
		this.mimeTypesConfiguration = mimeTypesConfiguration;
		modifiedURI = "";
		absolutePath = "";
		mimeType = "";
	}

	public void resolve() {
			splitURI = URI.split("/");
			if (isURIAliased()) {
				resolveURIAlias();
			} else if (isScriptAliased()) {
				resolveScriptAlias();
			} else {
				resolvePath();
			}
			resolveAbsolutePath();
			setMimeType();
	}

	public void resolveScriptAlias() {
		for( int index = 1; index < splitURI.length; index++ ) {
			String currentPossibleAlias = splitURI[index];
			if( httpdConfiguration.containsScriptAliases("/" + currentPossibleAlias + "/" ) ){
				splitURI[index] = httpdConfiguration.getScriptAliases("/" + currentPossibleAlias + "/");
				modifiedURI += splitURI[index];
			} else if ( splitURI[index].contains(".") ){
				modifiedURI += splitURI[index];
			} else {
				modifiedURI += splitURI[index] + "/";
			}
		}
	}

	public boolean isURIAliased() {
		for( String possibleAlias: splitURI ){
			if( httpdConfiguration.containsAliases("/" + possibleAlias +"/") )
				return true;
		}
		return false;
	}

	public void resolveURIAlias() {
		for( int index = 1; index < splitURI.length; index++ ) {
			String currentPossibleAlias = splitURI[index];
			if( httpdConfiguration.containsAliases("/" + currentPossibleAlias + "/" ) ){
				splitURI[index] = httpdConfiguration.getAliases("/" + currentPossibleAlias + "/");
				modifiedURI += splitURI[index];
			} else if ( splitURI[index].contains(".") ){
				modifiedURI += splitURI[index];
			} else {
				modifiedURI += splitURI[index] + "/";
			}
		}
	}

	public void resolvePath() {
		modifiedURI = httpdConfiguration.getDocumentRoot();
		for( int index = 1; index < splitURI.length; index++ ) {
			if ( splitURI[index].contains(".") ) {
				modifiedURI += splitURI[index];
			} else {
				modifiedURI += splitURI[index] + "/";
			}
		}
	}

	public void resolveAbsolutePath() {
		if( isFile() ){
			absolutePath = modifiedURI;
		} else {
			absolutePath += modifiedURI + httpdConfiguration.getDirectoryIndex();
		}
	}

	public boolean isFile() {
		return modifiedURI.contains( "." );
	}

	public boolean isScriptAliased() {
		for( String possibleAlias: splitURI ){
			if( httpdConfiguration.containsScriptAliases("/" + possibleAlias +"/") ){
				isScript = true;
				return true;
			}
		}
		return false;
	}

	public void setMimeType() {
		int indexOfPeriod = absolutePath.indexOf(".");
		String fileExtention = absolutePath.substring(indexOfPeriod + 1);
		mimeType = "Content-Type: " + mimeTypesConfiguration.lookup(fileExtention);
	}

	public String getMimeType() {
		return mimeType;
	}

	public boolean getIsScript() {
		return isScript;
	}

	public String getAbsolutePath() {
		return this.absolutePath;
	}

	public byte[] getBody() {
		return this.body;
	}
}
