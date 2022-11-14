package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "labels")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label {
    private static final int MIN_V = 3;
    private static final int MAX_V = 1_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min = MIN_V, max = MAX_V)
    @Column(unique = true)
    private String name;

    public Label(final Long idValue) {
        this.id = idValue;
    }

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "task_label",
            joinColumns = @JoinColumn(name = "label_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private Set<Task> tasks;

    /**
     * The method is for prevention of a status deletion when he has tasks associated with.
     */
    @PreRemove
    public void checkTasksAssociationBeforeRemoval() {
        if (this.tasks != null) {
            if (this.tasks.size() > 0) {
                throw new RuntimeException("Can't remove a label that has been linked with tasks.");
            }
        }
    }
}
