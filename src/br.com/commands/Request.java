package commands;

import service.Contacts;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by MarioJ on 30/05/15.
 */
public abstract class Request {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpServlet servlet;
    protected Contacts contacts;

    public Request() {
    }

    public Request(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet, Contacts contacts) {
        this.request = request;
        this.response = response;
        this.servlet = servlet;
        this.contacts = contacts;
    }

    public abstract void execute(final int i) throws Exception;
}
