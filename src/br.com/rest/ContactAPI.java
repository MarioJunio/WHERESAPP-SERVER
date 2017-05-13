package rest;

import commands.GetRequest;
import commands.PostRequest;
import commands.Request;
import model.API;
import service.Contacts;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by MarioJ on 07/04/15.
 */
@WebServlet(value = "/api/contact", loadOnStartup = 1)
@MultipartConfig
public class ContactAPI extends HttpServlet {

    @Inject
    private Contacts contactsService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            final int TAG = getTAG(req);

            Request request = new GetRequest(req, resp, this, contactsService);
            System.out.println("######### GET #########");
            request.execute(TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            final int TAG = getTAG(req);

            Request request = new PostRequest(req, resp, this, contactsService);
            System.out.println("######### POST #########");
            request.execute(TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getTAG(HttpServletRequest req) {
        return Integer.parseInt(req.getParameter(API.TAG));
    }

}
