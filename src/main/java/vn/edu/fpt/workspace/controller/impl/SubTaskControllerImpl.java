package vn.edu.fpt.workspace.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.controller.SubTaskController;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.subtask.CreateSubTaskRequest;
import vn.edu.fpt.workspace.dto.request.subtask.UpdateSubTaskRequest;
import vn.edu.fpt.workspace.dto.response.subtask.CreateSubTaskResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskDetailResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskResponse;
import vn.edu.fpt.workspace.factory.ResponseFactory;
import vn.edu.fpt.workspace.service.SubTaskService;
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
public class SubTaskControllerImpl implements SubTaskController {
    private final ResponseFactory responseFactory;
    private final SubTaskService subTaskService;

    @Override
    public ResponseEntity<GeneralResponse<CreateSubTaskResponse>> createSubTask(String workspaceId, String taskId, CreateSubTaskRequest request) {
        return responseFactory.response(subTaskService.createSubTask(workspaceId, taskId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateSubTask(String workspaceId, String subtaskId, UpdateSubTaskRequest request) {
        subTaskService.updateSubTask(workspaceId, subtaskId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteSubTask(String taskId, String subtaskId) {
        subTaskService.deleteSubTask(taskId, subtaskId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetSubTaskResponse>>> getSubTask(String taskId) {
        return responseFactory.response(subTaskService.getSubTask(taskId));
    }

    @Override
    public ResponseEntity<GeneralResponse<GetSubTaskDetailResponse>> getSubTaskDetail(String subtaskId) {
        return responseFactory.response(subTaskService.getSubTaskDetail(subtaskId));
    }
}
