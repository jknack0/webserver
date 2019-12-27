package response;

import request.Request;
import resource.Resource;
import java.io.File;

public class ResponseFactory {
  private String verbType;
  private String absolutePath;
  private File file;
  private boolean badRequest = false;

  public Response getResponse(Request request, Resource resource) {
    if (badRequest) {
      return new BadRequestResponse(resource);
    }
    verbType = request.getVerb();
    absolutePath = resource.getAbsolutePath();
    file = new File(absolutePath);
    if (verbType.equals("PUT"))
      return new CreatedResponse(resource);
    if (fileExists(file)) {
      if (!(resource.isScriptAliased())) {
        if (verbType.equals("GET") || verbType.equals("POST"))
          return new OkResponse(resource, true);
      }
      if (verbType.equals("DELETE")) {
        return new NoContentResponse(resource);
      }
    }
    return new NotFoundResponse(resource);
  }

  public boolean fileExists(File file ){
    if(file.exists()) return true;
    else return false;
  }

  public void setBadRequest(boolean badRequest) {
    this.badRequest = badRequest;
  }
}


