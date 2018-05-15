package org.BinghamTSA.uCount.web;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.BinghamTSA.uCount.core.utilities.FileUploadUtilities;

/**
 * The ImageServlet serves images from the data directory when a user accesses the /images/
 * directory. If an image is not found, a default image is returned.
 */
@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {

  private File imageNotFoundFile;

  @Override
  public void init() {
    String imageNotFoundPath = "/resources/images/notFound.png";
    if (imageNotFoundPath != null) {
      try {
        imageNotFoundFile = new File(getServletContext().getResource(imageNotFoundPath).toURI());
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Finds the number sent at the end of the url and returns the picture associated with it through
   * the response
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String filename = request.getPathInfo().substring(1);
    File file = new File(FileUploadUtilities.IMAGE_UPLOADS_DIR, filename);

    if (!file.exists() || filename.isEmpty() || filename.equals("/"))
      file = imageNotFoundFile;

    if (file != null) {
      response.setHeader("Content-Type", getServletContext().getMimeType(filename));
      response.setHeader("Content-Length", String.valueOf(file.length()));
      response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
      Files.copy(file.toPath(), response.getOutputStream());
    }
  }

}
