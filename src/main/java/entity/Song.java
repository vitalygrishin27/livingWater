package entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "songs")
@Embeddable
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    public Song() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id == song.id &&
                Objects.equals(name, song.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
