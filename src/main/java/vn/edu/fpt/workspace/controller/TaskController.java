package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.task.CreateTaskRequest;
import vn.edu.fpt.workspace.dto.request.task.UpdateTaskRequest;
import vn.edu.fpt.workspace.dto.response.task.CreateTaskResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskDetailResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:40
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/tasks")
public interface TaskController {

    @PostMapping("/{workspace-id}/{sprint-id}/task")
    ResponseEntity<GeneralResponse<CreateTaskResponse>> createTask(@PathVariable(name = "workspace-id")String workspaceId,
                                                                   @PathVariable(name = "sprint-id")String sprintId, @RequestBody CreateTaskRequest request);

    @PutMapping("/{workspace-id}/{task-id}")
    ResponseEntity<GeneralResponse<Object>> updateTask(@PathVariable(name = "workspace-id")String workspaceId,
                                                       @PathVariable(name = "task-id") String taskId, @RequestBody UpdateTaskRequest request);

    @DeleteMapping("/{sprint-id}/tasks/{task-id}")
    ResponseEntity<GeneralResponse<Object>> deleteTask(@PathVariable(name = "sprint-id") String sprintId, @PathVariable(name = "task-id") String taskId);

    @GetMapping("/{sprint-id}/tasks")
    ResponseEntity<GeneralResponse<PageableResponse<GetTaskResponse>>> getTask(
            @PathVariable(name = "sprint-id") String sprintId
    );

    @GetMapping("/{task-id}")
    ResponseEntity<GeneralResponse<GetTaskDetailResponse>> getTaskDetail(@PathVariable(name = "task-id") String taskId);
}
