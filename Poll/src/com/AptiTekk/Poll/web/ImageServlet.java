package com.AptiTekk.Poll.web;

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

import com.AptiTekk.Poll.core.utilities.FileUploadUtilities;
import com.AptiTekk.Poll.core.utilities.PropertiesHelper;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {

	private File imageNotFoundFile;

	@Override
	public void init() {
		String imageNotFoundPath = PropertiesHelper.getApplicationProperty("not_found_image");
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