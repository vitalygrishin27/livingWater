package entity;

import java.util.Date;

public class BuilderMember {

    private int id;
    private String lastName;
    private String firstName;
    private String secondName;
    private Date birth;
    private String ensembleName;
    private int countOfMembers;
    private Gender gender;
    private Address address;
    private String passport;
    private String INN;
    private String boss;
    private Category category;
    private Song firstSong;
    private Song secondSong;
    //  private Mark firstMarkId;
    // private Mark secondMarkId;
    private boolean registration;
    private int turnNumber;

    private BuilderMember() {

    }

    public Member build() {
        return Member.createMember(this);
    }

    public static BuilderMember getBuilderMember() {
        return new BuilderMember();
    }


    public BuilderMember setId(int id) {
        this.id = id;
        return this;
    }

    public BuilderMember setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BuilderMember setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public BuilderMember setSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public BuilderMember setBirth(Date birth) {
        this.birth = birth;
        return this;
    }

    public BuilderMember setEnsembleName(String ensembleName) {
        this.ensembleName = ensembleName;
        return this;
    }

    public BuilderMember setCountOfMembers(int countOfMembers) {
        this.countOfMembers = countOfMembers;
        return this;
    }

    public BuilderMember setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public BuilderMember setAddress(Address address) {
        this.address = address;
        return this;
    }

    public BuilderMember setPassport(String passport) {
        this.passport = passport;
        return this;
    }

    public BuilderMember setINN(String INN) {
        this.INN = INN;
        return this;
    }

    public BuilderMember setBoss(String boss) {
        this.boss = boss;
        return this;
    }

    public BuilderMember setCategory(Category category) {
        this.category = category;
        return this;
    }

    public BuilderMember setFirstSong(Song firstSong) {
        this.firstSong = firstSong;
        return this;
    }

    public BuilderMember setSecondSong(Song secondSong) {
        this.secondSong = secondSong;
        return this;
    }

    public BuilderMember setRegistration(boolean registration) {
        this.registration = registration;
        return this;
    }

    public BuilderMember setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
        return this;
    }


    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public Date getBirth() {
        return birth;
    }

    public String getEnsembleName() {
        return ensembleName;
    }

    public int getCountOfMembers() {
        return countOfMembers;
    }

    public Gender getGender() {
        return gender;
    }

    public Address getAddress() {
        return address;
    }

    public String getPassport() {
        return passport;
    }

    public String getINN() {
        return INN;
    }

    public String getBoss() {
        return boss;
    }

    public Category getCategory() {
        return category;
    }

    public Song getFirstSong() {
        return firstSong;
    }

    public Song getSecondSong() {
        return secondSong;
    }

    public boolean isRegistration() {
        return registration;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
}
