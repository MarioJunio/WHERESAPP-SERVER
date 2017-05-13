package rest;

import json.JSONObject;
import model.Confirmation;
import model.Contact;
import service.Contacts;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by MarioJ on 07/05/15.
 */
@WebServlet(value = "/api/dev", loadOnStartup = 1)
public class DevAPI extends HttpServlet {

    private final String TAG = "TAG";
    private final int CREATE_CONTACT = 1;

    @Inject
    private Contacts contacts;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            // get parameter TAG to specify the action will be executed
            final int tag = Integer.parseInt(req.getParameter(TAG));

            switch (tag) {

                case CREATE_CONTACT:
                    create_contact(req, resp);
                    break;

            }

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void create_contact(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        JSONObject jsonResponse = new JSONObject();

        System.out.println("\n=========== Parametros ===========");

        String ddi = req.getParameter(Contact.DDI);
        String phone = req.getParameter(Contact.PHONE);
        String name = req.getParameter(Contact.NAME);

        System.out.printf("--> %s : %s\n--> %s : %s\n--> %s : %s\n", Contact.DDI, ddi, Contact.PHONE, phone, Contact.NAME, name);

        System.out.println("\n=========== FIM Parametros ===========\n");

        // Se o telefone existe, no banco obtem sua confirmacao
        Confirmation confirmation = contacts.getConfirmation(ddi, phone);

        // Se o telefone nao existir ainda, tenta criar a conta e retornar seu codigo de confirmacao e sua key
        if (confirmation == null) {

            Contact contact = new Contact();
            contact.setDdi(ddi);
            contact.setPhone(phone);
            contact.setName(name);

            System.out.printf("\nCreating account %s\n", ddi + phone);
            confirmation = contacts.createAccount(contact);
        }

        jsonResponse.put(Confirmation.NEW, confirmation.isNew());
        jsonResponse.put(Confirmation.KEY, confirmation.getKey());
        jsonResponse.put(Confirmation.CONFIRM_CODE, confirmation.getConfirmCode());

        System.out.println("\n=========== Resposta ===========");
        System.out.print(jsonResponse.toString());
        System.out.println("\n=========== FIM Resposta ===========\n");

        resp.getWriter().println(jsonResponse.toString());

    }

}
