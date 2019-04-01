package entity;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Embeddable
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column (name = "name", nullable = false)
    private String name;

    public Role() {
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


}
