package entity;

public class BuilderUserJury {
    private String userName;
    private String password;
    private Role role;
    private String firstName;
    private String secondName;
    private String lastName;
    private String office;


    public static BuilderUserJury getBuilderUserJury() {
        return new BuilderUserJury();
    }

    private BuilderUserJury() {

    }

    public BuilderUserJury setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public BuilderUserJury setPassword(String password) {
        this.password = password;
        return this;
    }

    public BuilderUserJury setRole(Role role) {
        this.role = role;
        return this;
    }

    public BuilderUserJury setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public BuilderUserJury setSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public BuilderUserJury setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BuilderUserJury setOffice(String office) {
        this.office = office;
        return this;
    }

    public User build(){
        return User.createUserJuryByBuilder(this);
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
