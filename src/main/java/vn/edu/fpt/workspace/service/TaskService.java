package vn.edu.fpt.workspace.service;

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
 * @created : 08/11/2022 - 09:34
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface TaskService {

    CreateTaskResponse createTask(String workspaceId, String sprintId, CreateTaskRequest request);

    void updateTask(String workspaceId, String taskId, UpdateTaskRequest request);

    void deleteTask(String sprintId, String taskId);

    PageableResponse<GetTaskResponse> getTask(String sprintId);

    GetTaskDetailResponse getTaskDetail(String taskId);
}
