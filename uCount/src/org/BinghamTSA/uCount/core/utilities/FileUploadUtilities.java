package org.BinghamTSA.uCount.core.utilities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;

import org.BinghamTSA.uCount.core.Service;

public class FileUploadUtilities {

	public static final File UPLOADS_DIR = new File(System.getProperty("jboss.server.data.dir"), "Poll_Uploads");
	public static File IMAGE_UPLOADS_DIR;

	static {
		if (!UPLOADS_DIR.exists())
			UPLOADS_DIR.mkdir();

		IMAGE_UPLOADS_DIR = new File(UPLOADS_DIR, "images");
		if (!IMAGE_UPLOADS_DIR.exists())
			IMAGE_UPLOADS_DIR.mkdir();
	}

	/**
	 * Uploads an image to a specified path.
	 * 
	 * @param part
	 *            The Part from the fileInput
	 * @param uploadsDirName
	 *            The uploads directory name. E.x.: "images"
	 * @param fileName
	 *            The new file name of the upload.
	 * @throws IOException
	 */
	public static void uploadImageToPath(Part imagePart, String fileName) throws IOException {
		deleteUploadedImage(fileName);

		InputStream input = imagePart.getInputStream();
		Files.copy(input, new File(IMAGE_UPLOADS_DIR, fileName).toPath());

	}

	/**
	 * Uploads an image to a specified path, and crops and/or resizes it to a
	 * desired width, using a 1:1 aspect ratio.
	 * 
	 * @param part
	 *            The Part from the fileInput
	 * @param fileName
	 *            The new file name of the upload.
	 * @param desiredWidth
	 *            The desired width for the image. (Will also be height; 1:1
	 *            aspect ratio)
	 * @throws IOException
	 */
	public static void uploadImageToPathAndCrop(Part imagePart, String fileName, int desiredWidth) throws IOException {

		uploadImageToPath(imagePart, fileName);

		File uploadedPath = new File(IMAGE_UPLOADS_DIR, fileName);

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
	 * @param fileName
	 *            The filename of the uploaded image.
	 */
	public static void deleteUploadedImage(String fileName) {
		File uploadedImageFile = new File(IMAGE_UPLOADS_DIR, fileName);
		if (uploadedImageFile.exists()) {
			uploadedImageFile.delete();
		}
	}

}
