package entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
@Entity
@Table (name = "users")
@Embeddable
@JsonPropertyOrder({"userName", "password", "role"})
public class User {
    private final String userName;
    private final String password;
    @Embedded
    private Role role;
    private String firstName;
    private String secondName;
    private String lastName;
    private String office;

    public User(String userName, String password, String firstName, String secondName, String lastName, String office, Role role) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.office = office;
        this.role=role;
    }



    public String getUserName() {
        return userName;
    }


    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOffice() {
        return office;
    }
}


