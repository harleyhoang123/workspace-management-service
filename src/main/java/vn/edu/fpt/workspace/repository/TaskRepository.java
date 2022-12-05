package vn.edu.fpt.workspace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.workspace.entity.Sprint;
import vn.edu.fpt.workspace.entity.Task;

import java.util.Optional;

/**
 * vn.edu.fpt.workspace.repository
 *
 * @author : Portgas.D.Ace
 * @created : 04/12/2022
 * @contact : 0339850697- congdung2510@gmail.com
 **/
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    Optional<Task> findBytaskName(String taskName);
}
