package commands;

import com.google.gson.Gson;
import json.JSONObject;
import model.Confirmation;
import model.Contact;
import service.Contacts;
import service.Images;
import util.Tag;

import javax.resource.NotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by MarioJ on 30/05/15.
 */
public class PostRequest extends Request {

    public PostRequest(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet, Contacts contacts) {
        super(request, response, servlet, contacts);
    }

    public void execute(final int i) throws Exception {

        if (i == Tag.HTTP.CHECK_ACCOUNT.ordinal())
            checkAccount();
        else if (i == Tag.HTTP.NAME_UPDATE.ordinal())
            updateName();
        else if (i == Tag.HTTP.STATUS_UPDATE.ordinal())
            updateStatus();
        else if (i == Tag.HTTP.PHOTO_UPDATE.ordinal())
            updatePhoto();
        else if (i == Tag.HTTP.SYNCRONIZE_CONTACTS.ordinal())
            syncronizeContacts();
        else if (i == Tag.HTTP.CHECK_UPDATES.ordinal())
            checkUpdates();

        System.out.println("\n");

    }

    private void checkAccount() throws IOException, SQLException {

        System.out.println("--> " + Tag.HTTP.CHECK_ACCOUNT);

        JSONObject jsonResponse = new JSONObject();

        System.out.println("=========== Parametros ===========");

        String ddi = request.getParameter(Contact.DDI);
        String phone = request.getParameter(Contact.PHONE);

        System.out.printf("--> %s : %s\n--> %s : %s\n", Contact.DDI, ddi, Contact.PHONE, phone);

        System.out.println("=========== FIM Parametros ===========");

        // Se o telefone existe, no banco obtem sua confirmacao
        Confirmation confirmation = contacts.getConfirmation(ddi, phone);

        // Se o telefone nao existir ainda, tenta criar a conta e retornar seu codigo de confirmacao e sua key
        if (confirmation == null) {
            confirmation = contacts.createAccount(ddi, phone);
        }

        jsonResponse.put(Confirmation.NEW, confirmation.isNew());
        jsonResponse.put(Confirmation.KEY, confirmation.getKey());
        jsonResponse.put(Confirmation.CONFIRM_CODE, confirmation.getConfirmCode());

        System.out.println("=========== Resposta ===========");
        System.out.println(jsonResponse.toString());
        System.out.println("=========== FIM Resposta ===========");

        response.getWriter().println(jsonResponse.toString());

    }

    private void updateName() throws IOException {

        boolean success = false;
        System.out.println("--> " + Tag.HTTP.NAME_UPDATE);

        JSONObject json = new JSONObject();
        System.out.println("=========== Parametros ===========");

        String cn = request.getParameter(Contact.DDI);
        String phone = request.getParameter(Contact.PHONE);
        String name = request.getParameter(Contact.NAME);

        System.out.printf("--> %s : %s\n--> %s : %s\n--> %s : %s\n", Contact.DDI, cn, Contact.PHONE, phone, Contact.NAME, name);
        System.out.println("=========== FIM Parametros ===========");

        try {
            success = contacts.updateName(cn, phone, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        json.put("success", success);

        response.getWriter().println(json.toString());

    }

    private void updateStatus() throws NotSupportedException {
    }

    private void updatePhoto() throws IOException, ServletException {

        System.out.println("--> " + Tag.HTTP.PHOTO_UPDATE);

        Images images = new Images();
        JSONObject json = new JSONObject();
        System.out.println("=========== Parametros ===========");

        String cn = request.getParameter(Contact.DDI);
        String phone = request.getParameter(Contact.PHONE);

        System.out.printf("--> %s : %s\n--> %s : %s\n", Contact.DDI, cn, Contact.PHONE, phone);

        Part image = request.getPart(Contact.PHOTO_URI);
        Part imageThumb = request.getPart(Contact.PHOTO_URI_THUMB);

        System.out.println("=========== FIM Parametros ===========");

        // Cria imagens no servidor
        images.saveProfilePhoto(cn, phone, image, imageThumb);

        json.put("success", true);

        response.getWriter().println(json.toString());

    }

    private void syncronizeContacts() throws IOException, SQLException {

        // get numbers as string json
        String numbersJSON = request.getParameter(Contact.CONTACTS_LIST);

        // parse array string json to array that represents numbers in each position
        String[] numbers = numbersJSON.replaceAll("[\\[\\]\\s]", "").split(",");

        System.out.println("========= Parametros =========");

        for (String number : numbers)
            System.out.print(number + " ");

        System.out.println();
        System.out.println("========= FIM Parametros =========");

        List<Contact> contactsList = contacts.syncronizeAll(numbers);

        if (!contactsList.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(new Gson().toJson(contactsList));
        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

    }

    private void checkUpdates() throws NotSupportedException, SQLException, IOException {

        System.out.println("========= Parametros =========");

        String jsonContacts = request.getParameter(Contact.CONTACTS_LIST);
        jsonContacts = jsonContacts.replaceAll("[\\[\\]]", "");
        String[] numbers = jsonContacts.split("},");

        System.out.printf("Phones: %s\n", jsonContacts);

        System.out.println("========= FIM Parametros =========");

        String jsonResponse = contacts.syncronize(numbers);

        System.out.println(jsonResponse);

        if (jsonResponse.isEmpty()) {
            System.out.println("NO CONTACTS UPDATED");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(jsonResponse);
        }

    }

}
