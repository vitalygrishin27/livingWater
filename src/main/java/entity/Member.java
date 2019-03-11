package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String lastName;
    private String firstName;
    private String secondName;
    private String ensembleName;
    private Date birth;
    private int countOfMembers;
    @Embedded
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;
    @Embedded
    private Address address;
    private String passport;
    private String INN;
    private String boss;
    @Embedded
    private Category category;
    @Embedded
    private Song firstSong;
    @Embedded
    private Song secondSong;
    //  private Mark firstMarkId;
    // private Mark secondMarkId;
    private boolean registration;
    private int turnNumber;


    private Member() {

    }


    public static Member createMember(BuilderMember builderMember) {
        Member member = new Member();
        member.id = builderMember.getId();
        member.lastName = builderMember.getLastName();
        member.firstName = builderMember.getFirstName();
        member.secondName = builderMember.getSecondName();
        member.birth = builderMember.getBirth();
        member.ensembleName = builderMember.getEnsembleName();
        member.countOfMembers = builderMember.getCountOfMembers();
        member.gender = builderMember.getGender();
        member.address = builderMember.getAddress();
        member.passport = builderMember.getPassport();
        member.INN = builderMember.getINN();
        member.boss = builderMember.getBoss();
        member.category = builderMember.getCategory();
        member.firstSong = builderMember.getFirstSong();
        member.secondSong = builderMember.getSecondSong();
        member.registration = builderMember.isRegistration();
        member.turnNumber = builderMember.getTurnNumber();
        return member;

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
