package entity;

import javax.persistence.*;

@Entity
@Table (name = "marks")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Embedded
    private User jury;
    @Embedded
    private Member member;
    @Embedded
    @Enumerated(EnumType.STRING)
    @Column(length = 11)
    private  MarkCriteria criteriaOfMark;
    private Song song;
    private int mark;
}
