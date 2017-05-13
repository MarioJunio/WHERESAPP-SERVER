package service;

import dao.ContactDAO;
import model.Confirmation;
import model.Contact;
import util.Key;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by MarioJ on 07/04/15.
 */
public class Contacts {

    @Inject
    private ContactDAO dao;

    public Confirmation createAccount(String ddi, String phone) {

        String[] keys = genKeyAndConfirmCode(ddi, phone);

        try {

            if (dao.insert(ddi, phone, keys[0], keys[1]))
                return new Confirmation(true, Key.encode(ddi, phone), keys[1]);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Confirmation createAccount(Contact contact) {

        String[] keys = genKeyAndConfirmCode(contact.getDdi(), contact.getPhone());

        try {
            if (dao.insert(contact))
                return new Confirmation(true, Key.encode(contact.getDdi(), contact.getPhone()), keys[1]);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Confirmation getConfirmation(String cn, String phone) throws SQLException {
        return dao.getConfirmation(cn, phone);
    }

    public boolean updateName(String cn, String phone, String newName) throws SQLException {
        return dao.updateName(cn, phone, newName);
    }

    public List<Contact> syncronizeAll(String... numbers) throws SQLException {
        return dao.syncronizeAll(numbers);
    }

    public String syncronize(String... numbers) throws SQLException {
        return dao.syncronize(numbers);
    }

    public Contact getContact(String phone) throws SQLException {
        return dao.getContact(phone);
    }

    /**
     * @param cn
     * @param phone
     * @return Key Hashed and Confirm Code
     */
    private String[] genKeyAndConfirmCode(String cn, String phone) {
        return new String[]{Key.generate(cn, phone), Key.generateConfirmCode()};
    }

}
