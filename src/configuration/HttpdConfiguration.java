package configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class HttpdConfiguration extends ConfigurationReader {


  private String serverRoot;
  private String documentRoot;
  private String logFile;
  private String accessFile;  // It would be equal to default as .htaccess
  private String directoryIndex = "index.html";
  private static final int DEFAULT_PORT = 8080;
  private int listenPort = DEFAULT_PORT;
  private HashMap<String, String> aliasesMap;
  private HashMap<String, String> scriptAliasesMap;

  public HttpdConfiguration(String filePath) {
    super(new File(filePath));
    aliasesMap = new HashMap<>();
    scriptAliasesMap = new HashMap<>();
  }

  public void load() throws Exception {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String line;

    while ((line = bufferedReader.readLine()) != null) {
      if (line.startsWith("#") || line.isEmpty()) {
        continue;
      }
      String pars[] = line.split(" ");
      if (pars[0].equals("ServerRoot")) {
        pars[1] = pars[1].replaceAll("\"", "");
        serverRoot = pars[1];
      }
      if (pars[0].equals("DocumentRoot")) {
        pars[1] = pars[1].replaceAll("\"", "");
        documentRoot = pars[1];
      }
      if (pars[0].equals("Listen")) {
        listenPort = Integer.parseInt(pars[1]);
      }
      if (pars[0].equals("LogFile")) {
        pars[1] = pars[1].replaceAll("\"", "");
        logFile = pars[1];
      }
      if (pars[0].equals("ScriptAlias")) {
        pars[2] = pars[2].replaceAll("\"", "");
        scriptAliasesMap.put(pars[1], pars[2]);
      }
      /**
       *  both of the alias are saved in the same dictonary
       * */
      if (pars[0].equals("Alias")) {
        pars[2] = pars[2].replaceAll("\"", "");
        aliasesMap.put(pars[1], pars[2]);
      }
      if (pars[0].equals("AccessFile")) {
        accessFile = pars[1];
      }
      if (pars[0].equals("DirectoryIndex")) {
        directoryIndex = pars[1];
      }
    }
  }

  public String getDocumentRoot() {
    return documentRoot;
  }

  public String getLogFile() {
    return logFile;
  }

  public String getDirectoryIndex() {
    return directoryIndex;
  }

  public int getListenPort() {
    return listenPort;
  }

  public boolean containsScriptAliases(String key) {
    return scriptAliasesMap.containsKey(key);
  }

  public String getScriptAliases(String key) {
    return scriptAliasesMap.get(key);
  }

  public String getAliases(String key) {
    return aliasesMap.get(key);
  }

  public boolean containsAliases(String key) {
    return aliasesMap.containsKey(key);
  }
}
