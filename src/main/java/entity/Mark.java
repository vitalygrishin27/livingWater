package entity;

import javax.persistence.*;

@Entity
@Table (name = "marks")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Embedded
    private User jury;
    @Embedded
    private Member member;
    @Enumerated(EnumType.STRING)
    @Column(length = 11)
    private MARKCRITERIA criteriaOfMark;
    @Embedded
    private Song song;
    private int value;


    private Mark() {

    }


    public static Mark createMark(BuilderMark builderMark) {
        Mark mark = new Mark();
        mark.id = builderMark.getId();
        mark.jury = builderMark.getJury();
        mark.member = builderMark.getMember();
        mark.criteriaOfMark = builderMark.getCriteriaOfMark();
        mark.song = builderMark.getSong();
        mark.value = builderMark.getValue();
        return mark;
    }


    public int getId() {
        return id;
    }

    public User getJury() {
        return jury;
    }

    public Member getMember() {
        return member;
    }

    public MARKCRITERIA getCriteriaOfMark() {
        return criteriaOfMark;
    }

    public Song getSong() {
        return song;
    }

    public int getValue() {
        return value;
    }
}
