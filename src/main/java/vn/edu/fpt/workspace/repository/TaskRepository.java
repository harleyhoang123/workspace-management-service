package vn.edu.fpt.workspace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.edu.fpt.workspace.entity.Task;

/**
 * vn.edu.fpt.workspace.repository
 *
 * @author : Portgas.D.Ace
 * @created : 04/12/2022
 * @contact : 0339850697- congdung2510@gmail.com
 **/

public interface TaskRepository extends MongoRepository<Task, String> {
}
