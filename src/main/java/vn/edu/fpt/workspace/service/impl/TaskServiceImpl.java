package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.constant.ActivityTypeEnum;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.task.CreateTaskRequest;
import vn.edu.fpt.workspace.dto.request.task.UpdateTaskRequest;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintDetailResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskResponse;
import vn.edu.fpt.workspace.dto.response.task.CreateTaskResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskDetailResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;
import vn.edu.fpt.workspace.entity.*;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.*;
import vn.edu.fpt.workspace.service.SubTaskService;
import vn.edu.fpt.workspace.service.TaskService;
import vn.edu.fpt.workspace.service.UserInfoService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * vn.edu.fpt.workspace.service.impl
 *
 * @author : Portgas.D.Ace
 * @created : 04/12/2022
 * @contact : 0339850697- congdung2510@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final ActivityRepository activityRepository;
    @Override
    public CreateTaskResponse createTask(String storiesId, CreateTaskRequest request) {
        Sprint sprint = sprintRepository.findById(storiesId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Stories ID not exist"));

        String memberId = request.getMemberId();
        MemberInfo memberInfo = memberInfoRepository.findById(memberId).orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member Id not exists"));
        Activity activity = Activity.builder()
                .memberInfo(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("created the Issue")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        }catch (Exception ex){
            throw new BusinessException("Can't save activity in database: "+ ex.getMessage());
        }
        Task task = Task.builder()
                .taskName(request.getTaskName())
                .activities(List.of(activity))
                .build();
        try {
            task = taskRepository.save(task);
            log.info("Create task success");
        }catch (Exception ex){
            throw new BusinessException("Can't save new task to database: "+ ex.getMessage());
        }
        List<Task> currentTask = sprint.getTasks();
        currentTask.add(task);
        sprint.setTasks(currentTask);
        try {
            sprintRepository.save(sprint);
            log.info("Update sprint success");
        }catch (Exception ex){
            throw new BusinessException("Can't update sprint in database: "+ ex.getMessage());
        }
        return CreateTaskResponse.builder()
                .taskId(task.getTaskId())
                .build();
    }

    @Override
    public void updateTask(String taskId, UpdateTaskRequest request) {

    }

    @Override
    public void deleteTask(String taskId) {

    }

    @Override
    public PageableResponse<GetTaskResponse> getTask(String sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint ID not exist"));
        List<GetTaskResponse> getTaskResponses = sprint.getTasks().stream().map(this::convertTaskToGetTaskResponse).collect(Collectors.toList());
        return new PageableResponse<>(getTaskResponses);
    }

    private GetTaskResponse convertTaskToGetTaskResponse(Task task){
        return GetTaskResponse.builder()
                .taskId(task.getTaskId())
                .taskName(task.getTaskName())
                .estimate(task.getEstimate())
                .status(task.getStatus())
                .assignee(task.getAssignee())
                .build();
    }

    @Override
    public GetTaskDetailResponse getTaskDetail(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task ID not exist"));
        List<GetSubTaskResponse> getSubTaskResponses = task.getSubTasks().stream().map(this::convertSubTaskToGetSubTaskResponse).collect(Collectors.toList());
        List<ActivityResponse> activityResponses = task.getActivities().stream().map(this::convertActivityToActivityResponse).collect(Collectors.toList());
        return GetTaskDetailResponse.builder()
                .taskId(taskId)
                .taskName(task.getTaskName())
                .status(task.getStatus())
                .description(task.getDescription())
                .subTasks(getSubTaskResponses)
                .assignee(task.getAssignee())
                .label(task.getLabel())
                .reporter(task.getReporter())
                .activityResponses(activityResponses)
                .build();

    }

    private GetSubTaskResponse convertSubTaskToGetSubTaskResponse(SubTask subTask){
        return GetSubTaskResponse.builder()
                .subTaskId(subTask.getSubtaskId())
                .subTaskName(subTask.getSubtaskName())
                .estimate(subTask.getEstimate())
                .assignee(subTask.getAssignee())
                .status(subTask.getStatus())
                .build();
    }

    private ActivityResponse convertActivityToActivityResponse(Activity activity){

        return ActivityResponse.builder()
                .userInfo(userInfoService.getUserInfo(activity.getChangeBy().getAccountId()))
                .edited(activity.getChangedData())
                .createdDate(activity.getChangedDate())
                .activityType(activity.getType())
                .build();
    }
}