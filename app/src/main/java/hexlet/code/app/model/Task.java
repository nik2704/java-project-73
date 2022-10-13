package hexlet.code.app.model;

import lombok.AllArgsConstructor;
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
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private TaskStatus taskStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private User executor;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;


}
