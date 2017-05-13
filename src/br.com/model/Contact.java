package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by MarioJ on 06/04/15.
 */
@XmlRootElement
public class Contact implements Serializable {

    public static final String TABLE = "contacts";
    public static final String CONTACTS_LIST = "contacts";

    public static final String DDI = "ddi";
    public static final String PHONE = "phone";
    public static final String KEY = "_key";
    public static final String NAME = "name";
    public static final String DATE = "date_create";
    public static final String STATUS = "status";
    public static final String PHOTO_URI = "photo_uri";
    public static final String PHOTO_URI_THUMB = "photo_uri_tn";
    public static final String LAST_MODIFIED = "last_modified";
    public static final String LAST_SEE = "last_see";
    public static final String LAST_MODIFIED_TN = "last_modified_tn";
    public static final String CONFIRM_CODE = "confirm_code";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";

    private String ddi;
    private String phone;
    private String key;
    private String name;
    private Date date;
    private String status;
    private Date lastModified;
    private Date lastSee;
    private Date lastModifiedTn;
    private String confirmCode;
    private float latitude;
    private float longitude;

    public Contact() {
    }

    public Contact(String ddi, String phone, String key, String confirmCode, String name, Date date, String status, Date lastModified, Date lastSee,
                   Date lastModifiedTn, float latitude, float longitude) {

        this.ddi = ddi;
        this.phone = phone;
        this.key = key;
        this.name = name;
        this.date = date;
        this.status = status;
        this.lastModified = lastModified;
        this.lastSee = lastSee;
        this.lastModifiedTn = lastModifiedTn;
        this.confirmCode = confirmCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDdi() {
        return ddi;
    }

    public void setDdi(String ddi) {
        this.ddi = ddi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getLastSee() {
        return lastSee;
    }

    public void setLastSee(Date lastSee) {
        this.lastSee = lastSee;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public Date getLastModifiedTn() {
        return lastModifiedTn;
    }

    public void setLastModifiedTn(Date lastModifiedTn) {
        this.lastModifiedTn = lastModifiedTn;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "ddi='" + ddi + '\'' +
                ", phone='" + phone + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", lastSee=" + lastSee +
                ", confirmCode='" + confirmCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
