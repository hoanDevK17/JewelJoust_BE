package online.jeweljoust.BE.enums;

public enum AccountRole {

    MEMBER("Member"),
    STAFF("Staff"),
    ADMIN("Admin");
    private final String value;

    AccountRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
