package hexlet.code.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.PreRemove;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "statuses")
@NoArgsConstructor
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "taskStatus"
    )
    private List<Task> tasks;

    public TaskStatus(final Long idValue, String nameValue) {
        this.id = idValue;
        this.name = nameValue;
    }

    /**
     * The method is for prevention of a status deletion when he has tasks associated with.
     */
    @PreRemove
    public void checkTasksAssociationBeforeRemoval() {
        if (!this.tasks.isEmpty()) {
            throw new RuntimeException("Can't remove a status that has Tasks.");
        }
    }
}
