package vn.edu.fpt.workspace.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<GeneralResponse<CreateTaskResponse>> createTask(String storiesId, CreateTaskRequest request) {
        return responseFactory.response(taskService.createTask(storiesId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateTask(String taskId, UpdateTaskRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteTask(String taskId) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetTaskResponse>>> getTask(String taskId) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<GetTaskDetailResponse>> getTaskDetail(String taskId) {
        return null;
    }
}
