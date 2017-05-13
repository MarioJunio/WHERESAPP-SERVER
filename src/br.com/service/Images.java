package service;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

/**
 * Created by MarioJ on 24/04/15.
 */
public class Images {

    public static final String IMAGE_EXT = ".jpg", THUMB_SUFFIX = "_tn";
    public static final String PROFILE_IMAGES_PATH = "/opt/wheresapp/profile_photos/";
    public static final String PROFILE_IMAGES_THUMB_PATH = "/opt/wheresapp/profile_photos_thumb/";

    public String[] saveProfilePhoto(String cn, String phone, Part image, Part imageThumb) throws IOException {

        String filename = cn + phone;

        String imageName = PROFILE_IMAGES_PATH + filename + IMAGE_EXT;
        String imageThumbName = PROFILE_IMAGES_THUMB_PATH + filename + THUMB_SUFFIX + IMAGE_EXT;

        File imageFile = new File(imageName);
        File imageThumbFile = new File(imageThumbName);

        imageFile.createNewFile();
        imageThumbFile.createNewFile();

        final FileOutputStream fosImage = new FileOutputStream(imageFile);
        final FileOutputStream fosImageThumb = new FileOutputStream(imageThumbFile);

        new SaveImageDisk(fosImage, image.getInputStream()).start();
        new SaveImageDisk(fosImageThumb, imageThumb.getInputStream()).start();

        return new String[]{imageName, imageThumbName};

    }

    public File getImage(String cn, String phone) {
        return new File(PROFILE_IMAGES_PATH + (cn + phone) + IMAGE_EXT);
    }

    public File getImageThumb(String phone) {
        return new File(PROFILE_IMAGES_THUMB_PATH + phone + THUMB_SUFFIX + IMAGE_EXT);
    }

    public byte[] getImageThumbBytes(String phone) throws IOException {
        File img = getImageThumb(phone);

        if (img.exists())
            return Files.readAllBytes(img.toPath());

        return null;
    }

    private class SaveImageDisk extends Thread {

        private FileOutputStream fos;
        private InputStream inputSream;

        public SaveImageDisk(FileOutputStream fos, InputStream inputStream) {
            this.fos = fos;
            this.inputSream = inputStream;
        }

        @Override
        public void run() {

            byte[] buffer = new byte[1024];

            try {

                while (inputSream.read(buffer) != -1)
                    fos.write(buffer);

                inputSream.close();
                fos.close();

                System.out.println("--> File Saved.");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
