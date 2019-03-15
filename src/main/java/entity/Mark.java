package entity;

public class Mark {
    private int id;
    private User jury;
    private Member member;
    private MARKCRITERIA criteriaOfMark;
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
