package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    ResponseEntity<GeneralResponse<CreateTaskResponse>> createTask(@RequestBody CreateTaskRequest request);

    ResponseEntity<GeneralResponse<Object>> updateTask(@PathVariable(name = "task-id") String taskId, @RequestBody UpdateTaskRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteTask(@PathVariable(name = "task-id") String taskId);

    ResponseEntity<GeneralResponse<PageableResponse<GetTaskResponse>>> getTask(
            @RequestParam(name = "project-id") String projectId,
            @RequestParam(name = "status", required = false) String status
    );

    ResponseEntity<GeneralResponse<GetTaskDetailResponse>> getTaskDetail(@PathVariable(name = "task-id") String taskId);
}
