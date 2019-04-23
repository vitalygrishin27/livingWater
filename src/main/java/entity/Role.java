package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "name")
    public String name;

    @OneToMany(mappedBy = "role")
    public Set<User> users = new HashSet<User>();

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
