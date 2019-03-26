package entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table (name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;///
    @Column(name = "ensemble_name")
    private String ensembleName;
    private Date birth;
    @Column(name = "count_of_members")
    private int countOfMembers;
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;
    private String office;
    @Embedded
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Address address;
    private String passport;
    private String INN;
    private String boss;
    @Embedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Embedded
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song firstSong;
    @Embedded
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song secondSong;
    //  private Mark firstMarkId;
    // private Mark secondMarkId;
    private boolean registration;
    private int turnNumber;


    private Member() {

    }


    public static Member createMember(BuilderMember builderMember) {
        Member member = new Member();
        member.id=builderMember.getId();
        member.lastName=builderMember.getLastName();
        member.firstName=builderMember.getFirstName();
        member.secondName=builderMember.getSecondName();
        member.birth=builderMember.getBirth();
        member.ensembleName=builderMember.getEnsembleName();
        member.countOfMembers=builderMember.getCountOfMembers();
        member.gender=builderMember.getGender();
        member.office=builderMember.getOffice();
        member.address=builderMember.getAddress();
        member.passport=builderMember.getPassport();
        member.INN=builderMember.getINN();
        member.boss=builderMember.getBoss();
        member.category =builderMember.getCategory();
        member.firstSong=builderMember.getFirstSong();
        member.secondSong=builderMember.getSecondSong();
        member.registration=builderMember.isRegistration();
        member.turnNumber=builderMember.getTurnNumber();
        return member;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getId() {
        return id;
    }

    public Date getBirth() { return birth; }

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

    public String getOffice() {
        return office;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id == member.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


