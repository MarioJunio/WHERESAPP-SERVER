package model;

/**
 * Created by MarioJ on 07/04/15.
 */
public class Confirmation {

    public static final String NEW = "new";
    public static final String KEY = "key";
    public static final String CONFIRM_CODE = "confirm_code";

    private boolean isNew;
    private String key;
    private String confirmCode;

    public Confirmation(boolean isNew, String key, String confirmCode) {
        this.isNew = isNew;
        this.key = key;
        this.confirmCode = confirmCode;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }
}
