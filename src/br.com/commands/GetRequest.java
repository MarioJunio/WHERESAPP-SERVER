package commands;

import com.google.gson.Gson;
import model.Contact;
import service.Contacts;
import service.Images;
import util.Tag;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by MarioJ on 30/05/15.
 */
public class GetRequest extends Request {

    public GetRequest(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet, Contacts contactsService) {
        super(request, response, servlet, contactsService);
    }

    public void execute(final int i) throws IOException, SQLException {

        if (i == Tag.HTTP.PHOTO_TN_RETRIEVE.ordinal())
            getPhotoThumb();
        else if (i == Tag.HTTP.LAST_MODIFIED_TN_RETRIEVE.ordinal())
            getLastModifiedThumb();
        else if (i == Tag.HTTP.ACCOUNT_RETRIEVE.ordinal())
            account_retrieve();

        System.out.println("\n");

    }

    private void getPhotoThumb() throws IOException {

        System.out.println("============= Photo Thumb Retrieve =============");

        System.out.println("\n============= Parameters =============");
        String phone = request.getParameter(Contact.PHONE);
        Date lastModifiedTn = new Date(Long.parseLong(request.getParameter(Contact.LAST_MODIFIED_TN)));

        System.out.println(phone + "\n" + lastModifiedTn);

        System.out.println("============= END Parameters =============");

        File img = new Images().getImageThumb(phone);

        if (img != null && img.exists() && lastModifiedTn.compareTo(new Date(img.lastModified())) < 0) {

            System.out.println("Image Thumb Found\n\n");

            String mimeType = servlet.getServletContext().getMimeType(img.getAbsolutePath());
            long fileLength = img.length();

            response.setContentType(mimeType);
            response.setContentLength((int) fileLength);
            response.setStatus(HttpServletResponse.SC_OK);

            FileInputStream in = new FileInputStream(img);
            OutputStream out = response.getOutputStream();

            byte[] buffer = new byte[20480];

            int len;

            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();

        } else {
            System.out.println("Image Thumb not found\n\n");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

    }

    private void getLastModifiedThumb() throws IOException {

        System.out.println("============= Photo Thumb Last Modified Retrieve =============");

        System.out.println("\n============= Parameters =============");
        String phone = request.getParameter(Contact.PHONE);
        Date lastModifiedTn = new Date(Long.parseLong(request.getParameter(Contact.LAST_MODIFIED_TN)));

        System.out.println(phone + "\n" + lastModifiedTn);

        System.out.println("============= END Parameters =============");

        File img = new Images().getImageThumb(phone);
        long lastModified = img.lastModified();


        if (lastModifiedTn.compareTo(new Date(lastModified)) < 0) {

            response.setContentType("text/plain");
            response.setContentLength(String.valueOf(lastModified).length());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(lastModified);
            response.getWriter().close();

            System.out.println("Returning last modified tn " + lastModified + "\n\n");

        } else {
            System.out.println("Image Thumb last modified retrieve no content\n\n");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

    }

    private void account_retrieve() throws SQLException, IOException {

        System.out.println("============= Account Retrieve =============");

        System.out.println("\n============= Parameters =============");
        String phone = request.getParameter(Contact.PHONE);
        System.out.printf("%s\n", phone);
        System.out.println("============= END Parameters =============");

        // search in database if number exists
        Contact contact = contacts.getContact(phone);

        if (contact != null) {

            String json = new Gson().toJson(contact);

            System.out.println(json);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
            response.getWriter().close();

        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

}
