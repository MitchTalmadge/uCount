package com.AptiTekk.Poll.core.utilities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;

import com.AptiTekk.Poll.core.Service;

public class FileUploadUtilities {

    /**
     * Uploads a Part to a specified path.
     * 
     * @param part
     *            The Part from the fileInput
     * @param relativeDirectoryPath
     *            The directory location. e.x.: /resources/images/
     * @param fileName
     *            The new file name of the upload.
     * @throws IOException
     */
    public static void uploadPartToPath(Part part, String relativeDirectoryPath, String fileName) throws IOException {
	deleteUploadedImage(relativeDirectoryPath, fileName);
	
	InputStream input = part.getInputStream();
	Files.copy(input,
		new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativeDirectoryPath),
			fileName).toPath());

    }

    /**
     * Uploads a Part to a specified path, and crops and/or resizes it to a
     * desired width, using a 1:1 aspect ratio.
     * 
     * @param part
     *            The Part from the fileInput
     * @param relativeDirectoryPath
     *            The directory location. e.x.: /resources/images/
     * @param fileName
     *            The new file name of the upload.
     * @param desiredWidth
     *            The desired width for the image. (Will also be height; 1:1
     *            aspect ratio)
     * @throws IOException
     */
    public static void uploadPartToPathAndCrop(Part part, String relativeDirectoryPath, String fileName,
	    int desiredWidth) throws IOException {

	uploadPartToPath(part, relativeDirectoryPath, fileName);

	File uploadedPath = new File(
		FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativeDirectoryPath), fileName);

	BufferedImage bufferedImage = ImageIO.read(uploadedPath);

	BufferedImage scaledImage = null;

	// Scale the image up or down first
	if (bufferedImage.getWidth() < bufferedImage.getHeight()) {
	    int newSize = (int) (bufferedImage.getHeight() * ((double) desiredWidth / bufferedImage.getWidth()));
	    scaledImage = new BufferedImage(desiredWidth, newSize, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = scaledImage.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(bufferedImage, 0, 0, desiredWidth, newSize, null);
	    g.dispose();
	} else {
	    int newSize = (int) (bufferedImage.getWidth() * ((double) desiredWidth / bufferedImage.getHeight()));
	    scaledImage = new BufferedImage(newSize, desiredWidth, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = scaledImage.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(bufferedImage, 0, 0, newSize, desiredWidth, null);
	    g.dispose();
	}

	scaledImage = cropImage(scaledImage, new Rectangle((scaledImage.getWidth() / 2) - (desiredWidth / 2),
		(scaledImage.getHeight() / 2) - (desiredWidth / 2), desiredWidth, desiredWidth));

	ImageIO.write(scaledImage, "JPG", uploadedPath);
    }

    private static BufferedImage cropImage(BufferedImage src, Rectangle rect) {
	BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
	return dest;
    }

    /**
     * Deletes an uploaded image
     * 
     * @param relativeDirectoryPath
     *            The directory location. e.x.: /resources/images/
     * @param fileName
     *            The filename of the upload.
     */
    public static void deleteUploadedImage(String relativeDirectoryPath, String fileName) {
	if (!fileName.equals(Service.NOTFOUND_IMAGE_FILENAME)) {
	    File uploadedImageFile = new File(
		    FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativeDirectoryPath),
		    fileName);
	    if (uploadedImageFile.exists()) {
		uploadedImageFile.delete();
	    }
	}
    }

}
