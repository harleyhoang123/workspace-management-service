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
import vn.edu.fpt.workspace.dto.cache.UserInfo;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;
import vn.edu.fpt.workspace.dto.event.HandleNotifyEvent;
import vn.edu.fpt.workspace.dto.event.SendEmailEvent;
import vn.edu.fpt.workspace.dto.request.subtask.CreateSubTaskRequest;
import vn.edu.fpt.workspace.dto.request.subtask.UpdateSubTaskRequest;
import vn.edu.fpt.workspace.dto.response.subtask.CreateSubTaskResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskDetailResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;
import vn.edu.fpt.workspace.entity.*;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.*;
import vn.edu.fpt.workspace.service.SubTaskService;
import vn.edu.fpt.workspace.service.UserInfoService;

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
public class SubTaskServiceImpl implements SubTaskService {
    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    private final ActivityRepository activityRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final UserInfoService userInfoService;
    private final WorkspaceRepository workspaceRepository;
    private final AppConfigRepository appConfigRepository;
    private final SendEmailProducer sendEmailProducer;
    private final HandleNotifyProducer handleNotifyProducer;

    @Override
    public CreateSubTaskResponse createSubTask(String workspaceId, String taskId, CreateSubTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task ID not exist"));

        String memberId = request.getMemberId();
        MemberInfo memberInfo = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("Created sub-task \"" + request.getSubTaskName() + "\"")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        SubTask subTask = SubTask.builder()
                .subtaskName(request.getSubTaskName())
                .activities(List.of(activity))
                .build();
        try {
            subTask = subTaskRepository.save(subTask);
            log.info("Create subTask success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save new subTask to database: " + ex.getMessage());
        }
        List<SubTask> currentSubTask = task.getSubTasks();
        currentSubTask.add(subTask);
        task.setSubTasks(currentSubTask);
        try {
            taskRepository.save(task);
            log.info("Update task success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update task in database: " + ex.getMessage());
        }
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<MemberInfo> managers = workspace.getMembers().stream()
                .filter(v -> v.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole()) || v.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()))
                .collect(Collectors.toList());
        if (!managers.isEmpty()) {
            Optional<AppConfig> workspaceActivityTemplateId = appConfigRepository.findByConfigKey("CREATE_ISSUE_TEMPLATE_ID");
            if (workspaceActivityTemplateId.isPresent()) {
                for (MemberInfo member : managers) {
                    String memberEmail = userInfoService.getUserInfo(member.getAccountId()).getEmail();
                    SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                            .sendTo(memberEmail)
                            .bcc(null)
                            .cc(null)
                            .templateId(workspaceActivityTemplateId.get().getConfigValue())
                            .params(Map.of("ASSIGNED_BY", userInfoService.getUserInfo(member.getAccountId()).getFullName(), "TASK", subTask.getSubtaskName()))
                            .build();
                    sendEmailProducer.sendMessage(sendEmailEvent);
                }
            }
            for (MemberInfo member : managers) {
                handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                        .accountId(member.getAccountId())
                        .content(userInfoService.getUserInfo(member.getAccountId()).getFullName() + " created sub-task \"" + subTask.getSubtaskName() + "\"")
                        .createdDate(LocalDateTime.now())
                        .build());
            }
        }
        return CreateSubTaskResponse.builder()
                .subTaskId(subTask.getSubtaskId())
                .subTaskName(subTask.getSubtaskName())
                .build();
    }

    @Override
    public void updateSubTask(String workspaceId, String subtaskId, UpdateSubTaskRequest request) {
        SubTask subTask = subTaskRepository.findById(subtaskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "SubTask id not found"));
        MemberInfo memberInfo = memberInfoRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        List<Activity> activities = subTask.getActivities();
        if (Objects.nonNull(request.getSubTaskName())) {
            subTask.setSubtaskName(request.getSubTaskName());
        }
        if (Objects.nonNull(request.getDescription())) {
            subTask.setDescription(request.getDescription());
        }
        log.info("subtask Status:" + request.getStatus());
        if (Objects.nonNull(request.getStatus())) {
            if (request.getStatus().equals(WorkflowStatusEnum.TO_DO.getStatus())) {
                subTask.setStatus(WorkflowStatusEnum.TO_DO);
            } else if (request.getStatus()  .equals(WorkflowStatusEnum.IN_PROGRESS.getStatus())) {
                subTask.setStatus(WorkflowStatusEnum.IN_PROGRESS);
            } else if (request.getStatus().equals(WorkflowStatusEnum.DONE.getStatus())) {
                subTask.setStatus(WorkflowStatusEnum.DONE);
            }else {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Status invalid: "+ request.getStatus());
            }
        }
        if (Objects.nonNull(request.getLabel())) {
            subTask.setLabel(request.getLabel());
        }
        if (Objects.nonNull(request.getEstimate())) {
            subTask.setEstimate(request.getEstimate());
        }
        if (Objects.nonNull(request.getReporter())) {
            MemberInfo reporter = memberInfoRepository.findById(request.getReporter())
                    .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member ID not exist"));
            subTask.setReporter(reporter);
        }
        if (Objects.nonNull(request.getAssignee())) {
            MemberInfo assignee = memberInfoRepository.findById(request.getAssignee())
                    .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member ID not exist"));
            subTask.setAssignee(assignee);
            assignLogInSubTask(subTask, memberInfo, assignee);
        }

        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("Updated sub-task \"" + subTask.getSubtaskName() + "\"")
                .build();
        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        activities.add(activity);
        subTask.setActivities(activities);
        try {
            subTaskRepository.save(subTask);
            log.info("Update subTask success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save subTask to database : " + ex.getMessage());
        }

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<MemberInfo> managers = workspace.getMembers().stream()
                .filter(v -> v.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole()) || v.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()))
                .collect(Collectors.toList());
        if (!managers.isEmpty()) {
            Optional<AppConfig> workspaceActivityTemplateId = appConfigRepository.findByConfigKey("UPDATE_ISSUE_TEMPLATE_ID");
            if (workspaceActivityTemplateId.isPresent()) {
                for (MemberInfo member : managers) {
                    String memberEmail = userInfoService.getUserInfo(member.getAccountId()).getEmail();
                    SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                            .sendTo(memberEmail)
                            .bcc(null)
                            .cc(null)
                            .templateId(workspaceActivityTemplateId.get().getConfigValue())
                            .params(Map.of("ASSIGNED_BY", userInfoService.getUserInfo(member.getAccountId()).getFullName(), "TASK", subTask.getSubtaskName()))
                            .build();
                    sendEmailProducer.sendMessage(sendEmailEvent);
                }
            }
            for (MemberInfo member : managers) {
                handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                        .accountId(member.getAccountId())
                        .content(userInfoService.getUserInfo(member.getAccountId()).getFullName() + " updated sub-task \"" + subTask.getSubtaskName() + "\"")
                        .createdDate(LocalDateTime.now())
                        .build());
            }
        }
    }

    private void assignLogInSubTask(SubTask subTask, MemberInfo memberInfo, MemberInfo assignee) {
        Optional<AppConfig> assignTaskTemplateId = appConfigRepository.findByConfigKey("ASSIGN_ISSUE_TEMPLATE_ID");
        if (assignTaskTemplateId.isPresent()) {
            String memberEmail = userInfoService.getUserInfo(assignee.getAccountId()).getEmail();
            SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                    .sendTo(memberEmail)
                    .bcc(null)
                    .cc(null)
                    .templateId(assignTaskTemplateId.get().getConfigValue())
                    .params(Map.of("ASSIGNED_BY", userInfoService.getUserInfo(memberInfo.getAccountId()).getFullName(), "TASK", subTask.getSubtaskName()))
                    .build();
            sendEmailProducer.sendMessage(sendEmailEvent);
        }
        handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                .accountId(assignee.getAccountId())
                .content(userInfoService.getUserInfo(memberInfo.getAccountId()).getFullName() + " assigned sub-task \"" + subTask.getSubtaskName() + "\" to you")
                .createdDate(LocalDateTime.now())
                .build());
    }

    @Override
    public void deleteSubTask(String taskId, String subtaskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task ID not exist"));
        List<SubTask> subTasks = task.getSubTasks();
        if (subTasks.stream().noneMatch(m -> m.getSubtaskId().equals(subtaskId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Subtask not exist in Task");
        }
        subTasks.removeIf(m -> m.getSubtaskId().equals(subtaskId));
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
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Task ID not exist"));
        List<GetSubTaskResponse> getSubTaskResponses = task.getSubTasks().stream().map(this::convertSubTaskToSubGetTaskResponse).collect(Collectors.toList());
        return new PageableResponse<>(getSubTaskResponses);
    }

    private GetSubTaskResponse convertSubTaskToSubGetTaskResponse(SubTask subTask) {
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

    @Override
    public GetSubTaskDetailResponse getSubTaskDetail(String subtaskId) {
        SubTask subTask = subTaskRepository.findById(subtaskId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "SubTask ID not exist"));
        List<ActivityResponse> activityResponses = subTask.getActivities().stream().map(this::convertActivityToActivityResponse).collect(Collectors.toList());

        GetSubTaskDetailResponse getSubTaskDetailResponse = GetSubTaskDetailResponse.builder()
                .subTaskId(subtaskId)
                .subTaskName(subTask.getSubtaskName())
                .status(subTask.getStatus())
                .assignee(ConvertMemberInfoToUserInfoResponse(subTask.getAssignee()))
                .label(subTask.getLabel())
                .estimate(subTask.getEstimate())
                .reporter(ConvertMemberInfoToUserInfoResponse(subTask.getReporter()))
                .activityResponse(activityResponses)
                .description(subTask.getDescription())
                .build();
        return getSubTaskDetailResponse;
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

    private ActivityResponse convertActivityToActivityResponse(Activity activity) {

        return ActivityResponse.builder()
                .userInfo(userInfoService.getUserInfo(activity.getChangeBy().getAccountId()))
                .edited(activity.getChangedData())
                .createdDate(activity.getChangedDate())
                .activityType(activity.getType())
                .build();
    }
}
