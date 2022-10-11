package hexlet.code.app.repository;


import hexlet.code.app.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
//    Optional<Status> findByName(String name);
}
