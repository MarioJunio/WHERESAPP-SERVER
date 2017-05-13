package dao;

import com.google.gson.Gson;
import model.Confirmation;
import model.Contact;
import util.Utils;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MarioJ on 06/04/15.
 */
public class ContactDAO implements Serializable {

    @Inject
    private Connection connection;

    public ContactDAO() {
    }

    public boolean insert(String ddi, String phone, String key, String confirmCode) throws SQLException {

        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)", Contact.TABLE, Contact.DDI, Contact.PHONE, Contact.KEY, Contact.CONFIRM_CODE, Contact.LAST_SEE, Contact.LAST_MODIFIED);

        System.out.println(sql);

        Timestamp now = new Timestamp(new Date().getTime());

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, ddi);
        preparedStatement.setString(2, phone);
        preparedStatement.setString(3, key);
        preparedStatement.setString(4, confirmCode);
        preparedStatement.setTimestamp(5, now);
        preparedStatement.setTimestamp(6, now);

        boolean ok = preparedStatement.executeUpdate() > 0;

        preparedStatement.close();

        return ok;
    }

    public boolean insert(Contact contact) throws SQLException {

        String sql = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Contact.TABLE);

        System.out.println(sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, contact.getDdi());
        preparedStatement.setString(2, contact.getPhone());
        preparedStatement.setString(3, contact.getKey());
        preparedStatement.setString(4, contact.getConfirmCode());
        preparedStatement.setString(5, contact.getName());
        preparedStatement.setDate(6, new java.sql.Date(contact.getDate().getTime()));
        preparedStatement.setString(7, contact.getStatus());
        preparedStatement.setDate(8, new java.sql.Date(contact.getLastSee().getTime()));
        preparedStatement.setFloat(9, contact.getLatitude());
        preparedStatement.setFloat(10, contact.getLongitude());

        boolean ok = preparedStatement.executeUpdate() > 0;

        preparedStatement.close();

        return ok;

    }

    public Confirmation getConfirmation(String ddi, String phone) throws SQLException {

        Confirmation confirmation = null;
        String sql = String.format("SELECT %s, %s FROM %s WHERE %s = ? AND %s = ?", Contact.KEY, Contact.CONFIRM_CODE, Contact.TABLE, Contact.DDI, Contact.PHONE);

        System.out.println(sql);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, ddi);
        preparedStatement.setString(2, phone);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            confirmation = new Confirmation(false, rs.getString(Contact.KEY), rs.getString(Contact.CONFIRM_CODE));
        }

        return confirmation;
    }

    public boolean updateName(String cn, String phone, String newName) throws SQLException {

        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ? AND %s = ?", Contact.TABLE, Contact.NAME, Contact.DDI, Contact.PHONE);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, newName);
        preparedStatement.setString(2, cn);
        preparedStatement.setString(3, phone);

        System.out.println(sql);

        boolean isOk = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();

        return isOk;

    }

    public List<Contact> syncronizeAll(String... numbers) throws SQLException {

        List<Contact> contacts = new LinkedList<Contact>();

        String sql = String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE concat(%s, %s) in(%s)", Contact.DDI, Contact.PHONE, Contact.NAME, Contact.STATUS,
                Contact.LAST_MODIFIED, Contact.LAST_SEE, Contact.LATITUDE, Contact.LONGITUDE, Contact.TABLE, Contact.DDI, Contact.PHONE, Utils.joinArray(numbers, ","));

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();

        System.out.println(sql);

        while (rs.next()) {

            String ddi = rs.getString(Contact.DDI);
            String phone = rs.getString(Contact.PHONE);
            long lastModified = rs.getTimestamp(Contact.LAST_MODIFIED) != null ? rs.getTimestamp(Contact.LAST_MODIFIED).getTime() : 0L;
            long lastSee = rs.getTimestamp(Contact.LAST_SEE) != null ? rs.getTimestamp(Contact.LAST_SEE).getTime() : 0L;

            Contact c = new Contact(ddi, phone, null, null, rs.getString(Contact.NAME), null, rs.getString(Contact.STATUS),
                    new Date(lastModified), new Date(lastSee), null, rs.getFloat(Contact.LATITUDE), rs.getFloat(Contact.LONGITUDE));

            contacts.add(c);

        }

        return contacts;
    }

    public String syncronize(String... contactsStr) throws SQLException {

        String json = "[";

        for (String contact : contactsStr) {

            contact = contact.replaceAll("[\\{\\}]", "");

            String[] tokens = contact.split(",");

            String phone = tokens[0].split(":")[1].replaceAll("[\"]", "");
            long lastModified = Long.parseLong(tokens[1].split(":")[1]);

            String sql = String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE CONCAT(%s, %s) = ? AND %s > ?", Contact.DDI, Contact.PHONE, Contact.NAME, Contact.STATUS,
                    Contact.LAST_MODIFIED, Contact.LAST_SEE, Contact.LATITUDE, Contact.LONGITUDE, Contact.TABLE, Contact.DDI, Contact.PHONE, Contact.LAST_MODIFIED);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, phone);
            preparedStatement.setTimestamp(2, new Timestamp(lastModified));

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                Timestamp getLastModified = rs.getTimestamp(Contact.LAST_MODIFIED);
                Timestamp getLastSee = rs.getTimestamp(Contact.LAST_SEE);

                Contact c = new Contact();
                c.setDdi(rs.getString(Contact.DDI));
                c.setPhone(rs.getString(Contact.PHONE));
                c.setName(rs.getString(Contact.NAME));
                c.setStatus(rs.getString(Contact.STATUS));
                c.setLastModified(new Date(getLastModified != null ? getLastModified.getTime() : 0));
                c.setLastSee(new Date(getLastSee != null ? getLastSee.getTime() : 0));
                c.setLatitude(rs.getFloat(Contact.LATITUDE));
                c.setLongitude(rs.getFloat(Contact.LONGITUDE));

                json += new Gson().toJson(c);
                json += ",";
            }

        }

        json = json.substring(0, json.length() - 1);

        if (!json.isEmpty())
            json += "]";

        return json;
    }

    public Contact getContact(String phone) throws SQLException {

        Contact contact = null;

        String sql = String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE concat(%s, %s) = ?", Contact.DDI, Contact.PHONE, Contact.NAME, Contact.STATUS,
                Contact.LAST_MODIFIED, Contact.LAST_SEE, Contact.LATITUDE, Contact.LONGITUDE, Contact.TABLE, Contact.DDI, Contact.PHONE);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, phone);

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.first()) {

            String ddi = rs.getString(Contact.DDI);
            String getPhone = rs.getString(Contact.PHONE);
            long lastModified = rs.getTimestamp(Contact.LAST_MODIFIED) != null ? rs.getTimestamp(Contact.LAST_MODIFIED).getTime() : 0L;
            long lastSee = rs.getTimestamp(Contact.LAST_SEE) != null ? rs.getTimestamp(Contact.LAST_SEE).getTime() : 0L;

            contact = new Contact(ddi, getPhone, null, null, rs.getString(Contact.NAME), null, rs.getString(Contact.STATUS),
                    new Date(lastModified), new Date(lastSee), null, rs.getFloat(Contact.LATITUDE), rs.getFloat(Contact.LONGITUDE));
        }

        return contact;
    }

}
