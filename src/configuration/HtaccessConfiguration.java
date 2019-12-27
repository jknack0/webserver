package configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class HtaccessConfiguration extends ConfigurationReader {
  private HtpasswordConfiguration htPassword;
  private String authUserPath;
  private String authType;
  private String authName;
  private String require;
  private boolean validUser;    //Any type of user
  private HashMap<String,Boolean> allowedUsers;  //For users listed instead of valid-user. Setting their permission

  public HtaccessConfiguration(File file) throws Exception {
    super(file);
    //this.file = file;
    allowedUsers = new HashMap<>();
    load();
  }

  public void load() throws Exception {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String line;

    while ((line = bufferedReader.readLine()) != null) {
      if (line.startsWith("#") || line.isEmpty()) {
        continue;
      }
      String[] pars = line.split(" ");
      switch(pars[0]){
        case "AuthUserFile":
          pars[1] = pars[1].replaceAll("\"", "");
          authUserPath = pars[1];
          htPassword = new HtpasswordConfiguration(authUserPath);
          htPassword.load();
          break;
        case "AuthType":
          authType = pars[1];
          break;
        case "AuthName":
          line = line.replaceAll("AuthName", "");
          line = line.replaceAll("\"", "");
          authName = line;
          break;
        case "Require":
          require = pars[1];
        if(require.equals("valid-user")) validUser = true;
        if(require.equals("user")){
          int numUser = pars.length;
          int index = 2;
          while(index < numUser) {
            allowedUsers.put(pars[index],true);
            index++;
          }
        }
        break;
      }
    }
  }
}
