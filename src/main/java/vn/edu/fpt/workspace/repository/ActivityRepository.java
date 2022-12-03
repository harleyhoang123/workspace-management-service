package vn.edu.fpt.workspace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.workspace.entity.Activity;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 04/12/2022 - 00:36
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
}
