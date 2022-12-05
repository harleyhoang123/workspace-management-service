package vn.edu.fpt.workspace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.workspace.entity.Sprint;
import vn.edu.fpt.workspace.entity.SubTask;

import java.util.Optional;

@Repository
public interface SubTaskRepository extends MongoRepository<SubTask, String> {
    Optional<SubTask> findBySubtaskName(String subtaskName);
}
