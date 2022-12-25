package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.config.kafka.producer.HandleNotifyProducer;
import vn.edu.fpt.workspace.config.kafka.producer.SendEmailProducer;
import vn.edu.fpt.workspace.constant.ActivityTypeEnum;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;
import vn.edu.fpt.workspace.dto.event.HandleNotifyEvent;
import vn.edu.fpt.workspace.dto.event.SendEmailEvent;
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
import vn.edu.fpt.workspace.service.WorkspaceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    private final MemberInfoRepository memberInfoRepository;
    private final UserInfoService userInfoService;
    private final SubTaskRepository subTaskRepository;
    private final SubTaskService subTaskService;
    private final WorkspaceRepository workspaceRepository;
    private final AppConfigRepository appConfigRepository;
    private final SendEmailProducer sendEmailProducer;
    private final HandleNotifyProducer handleNotifyProducer;

    @Override
    public CreateTaskResponse createTask(String workspaceId, String sprintId, CreateTaskRequest request) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Stories ID not exist"));

        String memberId = request.getMemberId();
        MemberInfo memberInfo = memberInfoRepository.findById(memberId).orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member Id not exists"));
        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("Created sprint \"" + request.getTaskName() + "\"")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        Task task = Task.builder()
                .taskName(request.getTaskName())
                .activities(List.of(activity))
                .build();
        try {
            task = taskRepository.save(task);
            log.info("Create task success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save new task to database: " + ex.getMessage());
        }
        List<Task> currentTask = sprint.getTasks();
        currentTask.add(task);
        sprint.setTasks(currentTask);
        try {
            sprintRepository.save(sprint);
            log.info("Update sprint success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update sprint in database: " + ex.getMessage());
        }
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<MemberInfo> managers = workspace.getMembers().stream()
                .filter(v -> v.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole()) || v.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()))
                .collect(Collectors.toList());
        if(!managers.isEmpty()){
            Optional<AppConfig> orderMaterialTemplateId = appConfigRepository.findByConfigKey("WORKSPACE_ACTIVITY_TEMPLATE_ID");
            if(orderMaterialTemplateId.isPresent()) {
                for (MemberInfo member : managers) {
                    String memberEmail = userInfoService.getUserInfo(member.getAccountId()).getEmail();
                    SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                            .sendTo(memberEmail)
                            .bcc(null)
                            .cc(null)
                            .templateId(orderMaterialTemplateId.get().getConfigValue())
                            .params(null)
                            .build();
                    sendEmailProducer.sendMessage(sendEmailEvent);
                    handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                            .accountId(member.getAccountId())
                            .content(userInfoService.getUserInfo(member.getAccountId()).getFullName() + " created task \"" + task.getTaskName() + "\"")
                            .createdDate(LocalDateTime.now())
                            .build());
                }
            }
        }

        return CreateTaskResponse.builder()
                .taskId(task.getTaskId())
                .taskName(task.getTaskName())
                .build();
    }

    @Override
    public void updateTask(String workspaceId, String taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task id not exist"));
        MemberInfo memberInfo = memberInfoRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        List<Activity> activities = task.getActivities();

        if (Objects.nonNull(request.getTaskName())) {
            task.setTaskName(request.getTaskName());
        }
        if (Objects.nonNull(request.getDescription())) {
            task.setDescription(request.getDescription());
        }
        if (Objects.nonNull(request.getStatus())) {
            if (request.getStatus().equals(WorkflowStatusEnum.TO_DO.getStatus())){
                task.setStatus(WorkflowStatusEnum.TO_DO);
            }
            else if (request.getStatus().equals(WorkflowStatusEnum.IN_PROGRESS.getStatus())){
                task.setStatus(WorkflowStatusEnum.IN_PROGRESS);
            }
            else if (request.getStatus().equals(WorkflowStatusEnum.DONE.getStatus())){
                task.setStatus(WorkflowStatusEnum.DONE);
            }
            else  {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Status invalid: "+ request.getStatus());
            }
        }

        if (Objects.nonNull(request.getLabel())) {
            task.setLabel(request.getLabel());
        }
        if (Objects.nonNull(request.getEstimate())) {
            task.setEstimate(request.getEstimate());
        }
        if (Objects.nonNull(request.getReporter())) {
            MemberInfo reporter = memberInfoRepository.findById(request.getReporter())
                    .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member ID not exist"));
            task.setReporter(reporter);
        }
        if (Objects.nonNull(request.getAssignee())) {
            MemberInfo assignee = memberInfoRepository.findById(request.getAssignee())
                    .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member ID not exist"));
            task.setAssignee(assignee);
            assignLogInTask(task, memberInfo, assignee);
        }

        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("Updated task \"" + task.getTaskName() + "\"")
                .build();
        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        activities.add(activity);
        task.setActivities(activities);
        try {
            taskRepository.save(task);
            log.info("Update task success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save task to database : " + ex.getMessage());
        }
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<MemberInfo> managers = workspace.getMembers().stream()
                .filter(v -> v.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole()) || v.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()))
                .collect(Collectors.toList());
        if(!managers.isEmpty()){
            Optional<AppConfig> orderMaterialTemplateId = appConfigRepository.findByConfigKey("WORKSPACE_ACTIVITY_TEMPLATE_ID");
            if(orderMaterialTemplateId.isPresent()) {
                for (MemberInfo member : managers) {
                    String memberEmail = userInfoService.getUserInfo(member.getAccountId()).getEmail();
                    SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                            .sendTo(memberEmail)
                            .bcc(null)
                            .cc(null)
                            .templateId(orderMaterialTemplateId.get().getConfigValue())
                            .params(null)
                            .build();
                    sendEmailProducer.sendMessage(sendEmailEvent);
                    handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                            .accountId(activity.getChangeBy().getAccountId())
                            .content(userInfoService.getUserInfo(member.getAccountId()).getFullName() + " updated task \"" + task.getTaskName() + "\"")
                            .createdDate(activity.getChangedDate())
                            .build());
                }
            }
        }
    }

    private void assignLogInTask(Task task, MemberInfo memberInfo, MemberInfo assignee) {
        Optional<AppConfig> assignTaskTemplateId = appConfigRepository.findByConfigKey("WORKSPACE_ACTIVITY_TEMPLATE_ID");
        if (assignTaskTemplateId.isPresent()) {
            String memberEmail = userInfoService.getUserInfo(assignee.getAccountId()).getEmail();
            SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                    .sendTo(memberEmail)
                    .bcc(null)
                    .cc(null)
                    .templateId(assignTaskTemplateId.get().getConfigValue())
                    .params(null)
                    .build();
            sendEmailProducer.sendMessage(sendEmailEvent);
            handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                    .accountId(assignee.getAccountId())
                    .content(userInfoService.getUserInfo(memberInfo.getAccountId()).getFullName() + " assigned task \"" + task.getTaskName() + "\" to you")
                    .createdDate(LocalDateTime.now())
                    .build());
        }
    }

    @Override
    public void deleteTask(String sprintId, String taskId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint id not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task ID not exist"));
        List<Task> tasks = sprint.getTasks();
        if (tasks.stream().noneMatch(m -> m.getTaskId().equals(taskId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task is not exist in Sprint");
        }
        List<SubTask> subTaskList = task.getSubTasks();
        subTaskList.stream().map(SubTask::getSubtaskId).forEach((subTaskId) -> subTaskService.deleteSubTask(taskId, subTaskId));
        tasks.removeIf(v -> v.getTaskId().equals(taskId));
        sprint.setTasks(tasks);
        try {
            taskRepository.deleteById(taskId);
            log.info("Delete task success");
        } catch (Exception ex) {
            throw new BusinessException("Can't task sprint in database  " + ex.getMessage());
        }

        try {
            sprintRepository.save(sprint);
            log.info("Save sprint success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save sprint in database  " + ex.getMessage());
        }
    }

    @Override
    public PageableResponse<GetTaskResponse> getTask(String sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint ID not exist"));
        List<GetTaskResponse> getTaskResponses = sprint.getTasks().stream().map(this::convertTaskToGetTaskResponse).collect(Collectors.toList());
        return new PageableResponse<>(getTaskResponses);
    }

    private GetTaskResponse convertTaskToGetTaskResponse(Task task) {
        MemberInfo assignee = task.getAssignee();
        return GetTaskResponse.builder()
                .taskId(task.getTaskId())
                .taskName(task.getTaskName())
                .estimate(task.getEstimate())
                .status(task.getStatus())
                .assignee(assignee == null ? null : UserInfoResponse.builder()
                        .accountId(assignee.getAccountId())
                        .memberId(assignee.getMemberId())
                        .userInfo(userInfoService.getUserInfo(assignee.getAccountId()))
                        .build())
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
                .assignee(ConvertMemberInfoToUserInfoResponse(task.getAssignee()))
                .label(task.getLabel())
                .estimate(task.getEstimate())
                .reporter(ConvertMemberInfoToUserInfoResponse(task.getReporter()))
                .activityResponses(activityResponses)
                .build();

    }

    private UserInfoResponse ConvertMemberInfoToUserInfoResponse(MemberInfo memberInfo) {
        if (memberInfo == null) {
            return null;
        } else {
            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .accountId(memberInfo.getAccountId())
                    .memberId(memberInfo.getMemberId())
                    .userInfo(userInfoService.getUserInfo(memberInfo.getAccountId()))
                    .build();
            return userInfoResponse;
        }
    }

    private GetSubTaskResponse convertSubTaskToGetSubTaskResponse(SubTask subTask) {
        MemberInfo assignee = subTask.getAssignee();
        return GetSubTaskResponse.builder()
                .subTaskId(subTask.getSubtaskId())
                .subTaskName(subTask.getSubtaskName())
                .estimate(subTask.getEstimate())
                .assignee(assignee == null ? null : UserInfoResponse.builder()
                        .accountId(assignee.getAccountId())
                        .memberId(assignee.getMemberId())
                        .userInfo(userInfoService.getUserInfo(assignee.getAccountId()))
                        .build())
                .status(subTask.getStatus())
                .build();
    }

    private ActivityResponse convertActivityToActivityResponse(Activity activity) {

        return ActivityResponse.builder()
                .userInfo(userInfoService.getUserInfo(activity.getChangeBy().getAccountId()))
                .edited(activity.getChangedData())
                .createdDate(activity.getChangedDate())
                .activityType(activity.getType())
                .build();
    }
}
