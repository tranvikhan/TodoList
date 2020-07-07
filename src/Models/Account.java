package Models;

import javafx.scene.image.Image;
public class Account {
    private String username;
    private String password;
    private String fullName;
    private Image avatar;

    public Account( String username, String password, String fullName, Image avatar) {

        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.avatar = avatar;
    }

    public Account() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }
}
