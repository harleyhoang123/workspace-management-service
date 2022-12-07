package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.constant.ActivityTypeEnum;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.cache.UserInfo;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.subtask.CreateSubTaskRequest;
import vn.edu.fpt.workspace.dto.request.subtask.UpdateSubTaskRequest;
import vn.edu.fpt.workspace.dto.response.subtask.CreateSubTaskResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskDetailResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;
import vn.edu.fpt.workspace.entity.*;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.ActivityRepository;
import vn.edu.fpt.workspace.repository.MemberInfoRepository;
import vn.edu.fpt.workspace.repository.SubTaskRepository;
import vn.edu.fpt.workspace.repository.TaskRepository;
import vn.edu.fpt.workspace.service.SubTaskService;
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
public class SubTaskServiceImpl implements SubTaskService {
    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    private final ActivityRepository activityRepository;
    private final MemberInfoRepository memberInfoRepository;

    private final UserInfoService userInfoService;
    @Override
    public CreateSubTaskResponse createSubTask(String taskId, CreateSubTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task ID not exist"));

        String memberId = request.getMemberId();
        MemberInfo memberInfo = memberInfoRepository.findById(memberId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("created the Issue")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        }catch (Exception ex){
            throw new BusinessException("Can't save activity in database: "+ ex.getMessage());
        }
        SubTask subTask = SubTask.builder()
                .subtaskName(request.getSubTaskName())
                .activities(List.of(activity))
                .build();
        try {
            subTask = subTaskRepository.save(subTask);
            log.info("Create subTask success");
        }catch (Exception ex){
            throw new BusinessException("Can't save new subTask to database: "+ ex.getMessage());
        }
        List<SubTask> currentSubTask = task.getSubTasks();
        currentSubTask.add(subTask);
        task.setSubTasks(currentSubTask);
        try {
            taskRepository.save(task);
            log.info("Update task success");
        }catch (Exception ex){
            throw new BusinessException("Can't update task in database: "+ ex.getMessage());
        }
        return CreateSubTaskResponse.builder()
                .subTaskId(subTask.getSubtaskId())
                .subTaskName(subTask.getSubtaskName())
                .build();
    }

    @Override
    public void updateSubTask(String subtaskId, UpdateSubTaskRequest request) {
        SubTask subTask = subTaskRepository.findById(subtaskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "SubTask id not found"));

        if (Objects.nonNull(request.getSubTaskName())) {
            subTask.setSubtaskName(request.getSubTaskName());
        }
        if (Objects.nonNull(request.getDescription())) {
            subTask.setDescription(request.getDescription());
        }
        if (Objects.nonNull(request.getStatus())) {
            if (request.getStatus().equals(WorkflowStatusEnum.TO_DO))
                subTask.setStatus(WorkflowStatusEnum.TO_DO);
            if (request.getStatus().equals(WorkflowStatusEnum.IN_PROGRESS))
                subTask.setStatus(WorkflowStatusEnum.IN_PROGRESS);
            if (request.getStatus().equals(WorkflowStatusEnum.DONE))
                subTask.setStatus(WorkflowStatusEnum.DONE);
        }
        if (Objects.nonNull(request.getAssignee())) {
            MemberInfo memberInfo = memberInfoRepository.findById(request.getAssignee())
                    .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member ID not exist"));
            subTask.setAssignee(memberInfo);
        }
        if (Objects.nonNull(request.getLabel())) {
            subTask.setLabel(request.getLabel());
        }
        if (Objects.nonNull(request.getEstimate())) {
            subTask.setEstimate(request.getEstimate());
        }
        if (Objects.nonNull(request.getReporter())) {
            MemberInfo memberInfo = memberInfoRepository.findById(request.getReporter())
                    .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member ID not exist"));
            subTask.setReporter(memberInfo);
        }
        try {
            subTaskRepository.save(subTask);
            log.info("Update subTask success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save subTask to database : " + ex.getMessage());
        }
    }

    @Override
    public void deleteSubTask(String taskId, String subtaskId) {
        SubTask subTask = subTaskRepository.findById(subtaskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Subtask id not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task ID not exist"));
        List<SubTask> subTasks = task.getSubTasks();
        if (subTasks.stream().noneMatch(m->m.getSubtaskId().equals(subtaskId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Subtask not exist in Task");
        }
        subTasks.removeIf(m->m.getSubtaskId().equals(subtaskId));
        task.setSubTasks(subTasks);
        try {
            subTaskRepository.deleteById(subtaskId);
            log.info("Delete Subtask success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete subtask in database  " + ex.getMessage());
        }
        try {
            taskRepository.save(task);
            log.info("Save task success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save task in database  " + ex.getMessage());
        }
    }

    @Override
    public PageableResponse<GetSubTaskResponse> getSubTask(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task ID not exist"));
        List<GetSubTaskResponse> getSubTaskResponses = task.getSubTasks().stream().map(this::convertSubTaskToSubGetTaskResponse).collect(Collectors.toList());
        return new PageableResponse<>(getSubTaskResponses);
    }

    private GetSubTaskResponse convertSubTaskToSubGetTaskResponse(SubTask subTask){
        return GetSubTaskResponse.builder()
                .subTaskId(subTask.getSubtaskId())
                .subTaskName(subTask.getSubtaskName())
                .estimate(subTask.getEstimate())
                .assignee(subTask.getAssignee())
                .status(subTask.getStatus())
                .build();
    }

    @Override
    public GetSubTaskDetailResponse getSubTaskDetail(String subtaskId) {
        SubTask subTask = subTaskRepository.findById(subtaskId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "SubTask ID not exist"));
        List<ActivityResponse> activityResponses = subTask.getActivities().stream().map(this::convertActivityToActivityResponse).collect(Collectors.toList());

        GetSubTaskDetailResponse getSubTaskDetailResponse = GetSubTaskDetailResponse.builder()
                .subTaskId(subtaskId)
                .subTaskName(subTask.getSubtaskName())
                .status(subTask.getStatus())
                .assignee(subTask.getAssignee())
                .label(subTask.getLabel())
                .estimate(subTask.getEstimate())
                .reporter(subTask.getReporter())
                .activityResponse(activityResponses)
                .description(subTask.getDescription())
                .build();
        return getSubTaskDetailResponse;
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
