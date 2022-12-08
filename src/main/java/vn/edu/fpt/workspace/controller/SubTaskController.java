package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/{workspace-id}/{task-id}/subtask")
    ResponseEntity<GeneralResponse<CreateSubTaskResponse>> createSubTask(@PathVariable(name = "workspace-id")String workspaceId,
                                                                         @PathVariable(name = "task-id") String taskId, @RequestBody CreateSubTaskRequest request);

    @PutMapping("/{workspace-id}/{subtask-id}")
    ResponseEntity<GeneralResponse<Object>> updateSubTask(@PathVariable(name = "workspace-id")String workspaceId,
                                                          @PathVariable(name = "subtask-id") String subtaskId, @RequestBody UpdateSubTaskRequest request);

    @DeleteMapping("/{task-id}/subtasks/{subtask-id}")
    ResponseEntity<GeneralResponse<Object>> deleteSubTask(@PathVariable(name = "task-id") String taskId, @PathVariable(name = "subtask-id") String subtaskId);

    @GetMapping("/{task-id}/subtasks")
    ResponseEntity<GeneralResponse<PageableResponse<GetSubTaskResponse>>> getSubTask(
            @PathVariable(name = "task-id") String taskId
    );

    @GetMapping("/{subtask-id}")
    ResponseEntity<GeneralResponse<GetSubTaskDetailResponse>> getSubTaskDetail(@PathVariable(name = "subtask-id") String subtaskId);
}
