package mx.ipn.cidetec.virtual.entities;

/**
 * Created by sergio on 20/01/17.
 */
public class SuperUser {
    private User user;
    private Account account;
    private String password;

    public SuperUser( User user, Account account, String password ) {
        this.user = user;
        this.account = account;
        this.password = password;
    }

    public SuperUser() {
    }

    public User getUser() {
        return user;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount( Account account ) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }
}
