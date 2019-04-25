package entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import javax.persistence.*;
import java.util.Objects;

//@JsonPropertyOrder({"userName", "password", "role"})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Column(name = "username")
    public String userName;
    @Column(name = "password")
    public String password;
    @ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    public Role role;
    @Column(name = "first_name")
    public String firstName;
    @Column(name = "second_name")
    public String secondName;
    @Column(name = "last_name")
    public String lastName;
    @Column(name = "office")
    public String office;



    public User(String userName, String password, String firstName, String secondName, String lastName, String office, Role role) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.office = office;
        this.role = role;
    }



    public User() {
    }

    public static User createUserJuryByBuilder(BuilderUserJury builderUserJury) {
        return new User(builderUserJury.getUserName(),
                builderUserJury.getPassword(),
                builderUserJury.getFirstName(),
                builderUserJury.getSecondName(),
                builderUserJury.getLastName(),
                builderUserJury.getOffice(),
                builderUserJury.getRole());

    }

    public int getId() {
        return id;
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

    public void setUserName(String userName) {
        this.userName = userName;
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


