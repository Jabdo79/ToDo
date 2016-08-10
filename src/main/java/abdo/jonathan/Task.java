package abdo.jonathan;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Jonathan Abdo
 * Pojo for Tasks with Hibernate annotations
 */

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    @Type(type = "true_false")
    private boolean active = true;

    @Column
    private String description;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
