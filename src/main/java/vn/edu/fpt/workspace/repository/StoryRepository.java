package vn.edu.fpt.workspace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.workspace.entity.Story;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 22:48
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface StoryRepository extends MongoRepository<Story, String> {

}
