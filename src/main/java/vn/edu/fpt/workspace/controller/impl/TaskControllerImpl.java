package vn.edu.fpt.workspace.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.controller.TaskController;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.task.CreateTaskRequest;
import vn.edu.fpt.workspace.dto.request.task.UpdateTaskRequest;
import vn.edu.fpt.workspace.dto.response.task.CreateTaskResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskDetailResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;
import vn.edu.fpt.workspace.factory.ResponseFactory;
import vn.edu.fpt.workspace.service.TaskService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:42
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskControllerImpl implements TaskController{
    private final ResponseFactory responseFactory;
    private final TaskService taskService;


    @Override
    public ResponseEntity<GeneralResponse<CreateTaskResponse>> createTask(String workspaceId, String sprintId, CreateTaskRequest request) {
        return responseFactory.response(taskService.createTask(workspaceId, sprintId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateTask(String workspaceId, String taskId, UpdateTaskRequest request) {
        taskService.updateTask(workspaceId, taskId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteTask(String sprintId, String taskId) {
        taskService.deleteTask(sprintId, taskId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetTaskResponse>>> getTask(String sprintId) {
        return responseFactory.response(taskService.getTask(sprintId));
    }

    @Override
    public ResponseEntity<GeneralResponse<GetTaskDetailResponse>> getTaskDetail(String taskId) {
        return responseFactory.response(taskService.getTaskDetail(taskId));
    }
}
