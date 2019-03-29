package entity;

public class BuilderMark {
    private int id;
    private User jury;
    private Member member;
    private MARKCRITERIA criteriaOfMark;
    private Song song;
    private int value;


    public static BuilderMark getNewBuilderMark() {
        return new BuilderMark();
    }

    private BuilderMark() {

    }


    public BuilderMark setId(int id) {
        this.id = id;
        return this;
    }

    public BuilderMark setJury(User jury) {
        this.jury = jury;
        return this;
    }

    public BuilderMark setMember(Member member) {
        this.member = member;
        return this;
    }

    public BuilderMark setCriteriaOfMark(MARKCRITERIA criteriaOfMark) {
        this.criteriaOfMark = criteriaOfMark;
        return this;
    }

    public BuilderMark setSong(Song song) {
        this.song = song;
        return this;
    }

    public BuilderMark setValue(int value) {
        this.value = value;
        return this;
    }

    public Mark build() {
        return Mark.createMark(this);

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
