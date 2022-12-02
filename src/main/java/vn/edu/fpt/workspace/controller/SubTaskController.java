package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
 * @created : 05/11/2022 - 14:40
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/subtasks")
public interface SubTaskController {

    ResponseEntity<GeneralResponse<CreateSubTaskResponse>> createSubTask(@RequestBody CreateSubTaskRequest request);

    ResponseEntity<GeneralResponse<Object>> updateSubTask(@PathVariable(name = "subtask-id") String subtaskId, @RequestBody UpdateSubTaskRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteSubTask(@PathVariable(name = "subtask-id") String subtaskId);

    ResponseEntity<GeneralResponse<PageableResponse<GetSubTaskResponse>>> getSubTask(
            @RequestParam(name = "project-id") String projectId,
            @RequestParam(name = "status", required = false) String status
    );

    ResponseEntity<GeneralResponse<GetSubTaskDetailResponse>> getSubTaskDetail(@PathVariable(name = "subtask-id") String subtaskId);
}
