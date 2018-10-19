package domain;


public class User {

    private String login;

    private Role role;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(String login, Role role) {
        this.login = login;
        this.role = role;
    }
}
