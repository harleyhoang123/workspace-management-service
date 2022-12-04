package vn.edu.fpt.workspace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.edu.fpt.workspace.entity.SubTask;

public interface SubTaskRepository extends MongoRepository<SubTask, String> {
}
