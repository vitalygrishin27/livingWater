package entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder({"userName", "password", "role"})
public class User {
    private final String userName;
    private final String password;
    private String role;
    private String firstName;
    private String secondName;
    private String lastName;
    private String office;

    public User(String userName, String password, String firstName, String secondName, String lastName, String office) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.office = office;
    }



    public String getUserName() {
        return userName;
    }


    public String getPassword() {
        return password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName) &&
                password.equals(user.password);
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


