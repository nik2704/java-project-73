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
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.PreRemove;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Column(unique = true)
    private String email;

    @NotBlank
    @JsonIgnore
    private String password;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

    public User(final Long userId) {
        this.id = userId;
    }
    public User(final Long userId, String firstNameValue, String lastNameValue, String emailValue, String pwdValue) {
        this.id = userId;
        this.firstName = firstNameValue;
        this.lastName = lastNameValue;
        this.email = emailValue;
        this.password = pwdValue;
    }

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "author"
    )
    private List<Task> tasksLinkedAsAuthor;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "executor"
    )
    private List<Task> tasksLinkedAsExecutor;

    /**
     * The method is for prevention of a user deletion when he has tasks associated with.
     */
    @PreRemove
    public void checkTasksAssociationBeforeRemoval() {
        if ((!this.tasksLinkedAsAuthor.isEmpty()) && (!this.tasksLinkedAsExecutor.isEmpty())) {
            throw new RuntimeException("Can't remove a user that has Tasks (as author or executor).");
        }
    }

}
