package fgl.userPanel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private UserType type;
    private boolean isActivated;
    private boolean isBlocked;
    private String confirmationString;

    public User( String username, String email ) {

        this.username = username;
        this.email = email;
    }

    public User( Long id,
                 String name,
                 String surname,
                 String username,
                 String email,
                 UserType type,
                 boolean isActivated,
                 boolean isBlocked,
                 String password) {

        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.type = type;
        this.isActivated = isActivated;
        this.isBlocked = isBlocked;
        this.password = password;
    }

    public User( Long id,
                 String name,
                 String surname,
                 String username,
                 String email,
                 UserType type,
                 boolean isActivated,
                 boolean isBlocked){

        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.type = type;
        this.isActivated = isActivated;
        this.isBlocked = isBlocked;
    }


    public User(String name,
                    String surname,
                    String username,
                    String email,
                    String password) throws NoSuchAlgorithmException {

        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = encryptPassword(password);
    }

    public User(String name,
                String surname,
                String username,
                String email,
                String password,
                String confirmationString) throws NoSuchAlgorithmException {

        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = encryptPassword(password);
        this.confirmationString = confirmationString;
    }


    public User(String name,
                String surname,
                String username,
                String email) {

        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserType getType() {
        return type;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setSurname( String surname ) {
        this.surname = surname;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public void setType( UserType type ) {
        this.type = type;
    }

    public void setBlocked( boolean blocked ) {
        isBlocked = blocked;
    }

    public String getConfirmationString() {
        return confirmationString;
    }

    public void setConfirmationString(String confirmationString) {
        this.confirmationString = confirmationString;
    }

    public String encryptPassword(String plainPassword) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                plainPassword.getBytes(StandardCharsets.UTF_8));

        String codedPassword = bytesToHex(encodedhash);

        return codedPassword;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}