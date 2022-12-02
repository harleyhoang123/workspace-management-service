package vn.edu.fpt.workspace.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.subtask.CreateSubTaskRequest;
import vn.edu.fpt.workspace.dto.request.subtask.UpdateSubTaskRequest;
import vn.edu.fpt.workspace.dto.response.subtask.CreateSubTaskResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskDetailResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 08/11/2022 - 09:35
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface SubTaskService {

    CreateSubTaskResponse createSubTask(CreateSubTaskRequest request);

    void updateSubTask(String subtaskId, UpdateSubTaskRequest request);

    void deleteSubTask(String subtaskId);

    PageableResponse<GetSubTaskResponse> getSubTask(String projectId, String status);

    GetSubTaskDetailResponse getSubTaskDetail(String subtaskId);
}
