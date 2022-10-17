package hexlet.code.app.repository;

//import com.querydsl.core.types.dsl.StringPath;
import hexlet.code.app.model.QTask;
import hexlet.code.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task>,
        QuerydslBinderCustomizer<QTask> {

    @Override
    default void customize(QuerydslBindings bindings, QTask task) {
//        bindings.bind(task.name, task.description).first(
//                (StringPath path, String value) -> path.containsIgnoreCase(value)
//        );
//        bindings.excluding(task.executor.id);
    }
}
