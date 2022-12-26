package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.config.kafka.producer.HandleNotifyProducer;
import vn.edu.fpt.workspace.config.kafka.producer.SendEmailProducer;
import vn.edu.fpt.workspace.constant.ActivityTypeEnum;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;
import vn.edu.fpt.workspace.dto.event.HandleNotifyEvent;
import vn.edu.fpt.workspace.dto.event.SendEmailEvent;
import vn.edu.fpt.workspace.dto.request.sprint.CreateSprintRequest;
import vn.edu.fpt.workspace.dto.request.sprint.GetSprintContainerResponse;
import vn.edu.fpt.workspace.dto.request.sprint.UpdateSprintRequest;
import vn.edu.fpt.workspace.dto.response.sprint.CreateSprintResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintDetailResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;
import vn.edu.fpt.workspace.entity.*;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.*;
import vn.edu.fpt.workspace.service.SprintService;
import vn.edu.fpt.workspace.service.TaskService;
import vn.edu.fpt.workspace.service.UserInfoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 22:47
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ActivityRepository activityRepository;
    private final TaskService taskService;
    private final UserInfoService userInfoService;
    private final AppConfigRepository appConfigRepository;
    private final SendEmailProducer sendEmailProducer;
    private final MemberInfoRepository memberInfoRepository;
    private final HandleNotifyProducer handleNotifyProducer;

    @Override
    public CreateSprintResponse createSprint(String workspaceId, CreateSprintRequest request) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));

        if (workspace.getSprints().stream().anyMatch(m -> m.getSprintName().equals(request.getSprintName()))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint name is already exist in workspace");
        }
        String memberId = request.getMemberId();
        MemberInfo memberInfo = workspace.getMembers().stream().filter(m -> m.getMemberId().equals(memberId)).findAny()
                .orElseThrow(() -> new BusinessException("Member ID not contain in repository member"));

        if (!memberInfo.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()) && !memberInfo.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole())) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Invalid role for create sprint");
        }

        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("Created sprint \"" + request.getSprintName() + "\"")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        LocalDateTime currentDate = LocalDateTime.now();
        Sprint sprint = Sprint.builder()
                .sprintName(request.getSprintName())
                .activities(List.of(activity))
                .startDate(currentDate)
                .endDate(currentDate.plusDays(workspace.getSprintDuration()))
                .build();
        try {
            sprint = sprintRepository.save(sprint);
            log.info("Create sprint success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save new sprint to database: " + ex.getMessage());
        }
        List<Sprint> currentSprint = workspace.getSprints();
        currentSprint.add(sprint);
        workspace.setSprints(currentSprint);
        try {
            workspaceRepository.save(workspace);
            log.info("Update workspace success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update workspace in database: " + ex.getMessage());
        }

        List<MemberInfo> managers = workspace.getMembers().stream()
                .filter(v -> v.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole()) || v.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()))
                .collect(Collectors.toList());
        if (!managers.isEmpty()) {
            Optional<AppConfig> orderMaterialTemplateId = appConfigRepository.findByConfigKey("CREATE_ISSUE_TEMPLATE_ID");
            if (orderMaterialTemplateId.isPresent()) {
                for (MemberInfo member : managers) {
                    String memberEmail = userInfoService.getUserInfo(member.getAccountId()).getEmail();
                    SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                            .sendTo(memberEmail)
                            .bcc(null)
                            .cc(null)
                            .templateId(orderMaterialTemplateId.get().getConfigValue())
                            .params(Map.of("ASSIGNED_BY", userInfoService.getUserInfo(member.getAccountId()).getFullName(), "TASK", sprint.getSprintName()))
                            .build();
                    sendEmailProducer.sendMessage(sendEmailEvent);
                }
            }
            for (MemberInfo member : managers) {
                handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                        .accountId(member.getAccountId())
                        .content(userInfoService.getUserInfo(member.getAccountId()).getFullName() + " created sprint \"" + sprint.getSprintName() + "\"")
                        .createdDate(LocalDateTime.now())
                        .build());
            }
        }
        return CreateSprintResponse.builder()
                .sprintId(sprint.getSprintId())
                .status(sprint.getStatus())
                .sprintName(sprint.getSprintName())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .build();
    }

    @Override
    public void updateSprint(String workspaceId, String sprintId, UpdateSprintRequest request) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint id not found"));
        MemberInfo memberInfo = memberInfoRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        List<Activity> activities = sprint.getActivities();

        if (!sprint.getSprintName().equals(request.getSprintName())) {
            if (Objects.nonNull(request.getSprintName())) {
                if (sprintRepository.findBySprintName(request.getSprintName()).isPresent()) {
                    throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint name already in database");
                }
                log.info("Update sprint name: {}", request.getSprintName());
                sprint.setSprintName(request.getSprintName());
            }
        }
        if (Objects.nonNull(request.getGoal())) {
            log.info("Update goal: {}", request.getGoal());
            sprint.setGoal(request.getGoal());
        }
        if (Objects.nonNull(request.getStartDate())) {
            log.info("Update is start date: {}", request.getStartDate());
            sprint.setStartDate(request.getStartDate());
        }
        if (Objects.nonNull(request.getDueDate())) {
            log.info("Update is due date: {}", request.getDueDate());
            sprint.setEndDate(request.getDueDate());
        }
        if (Objects.nonNull(request.getStatus())) {
            sprint.setStatus(request.getStatus());
        }

        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("Updated sprint \"" + sprint.getSprintName() + "\"")
                .build();
        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        activities.add(activity);
        sprint.setActivities(activities);
        try {
            sprintRepository.save(sprint);
            log.info("Update sprint success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save sprint in database when update sprint: " + ex.getMessage());
        }
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<MemberInfo> managers = workspace.getMembers().stream()
                .filter(v -> v.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole()) || v.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()))
                .collect(Collectors.toList());
        if (!managers.isEmpty()) {
            Optional<AppConfig> orderMaterialTemplateId = appConfigRepository.findByConfigKey("UPDATE_ISSUE_TEMPLATE_ID");
            if (orderMaterialTemplateId.isPresent()) {
                for (MemberInfo member : managers) {
                    String memberEmail = userInfoService.getUserInfo(member.getAccountId()).getEmail();
                    SendEmailEvent sendEmailEvent = SendEmailEvent.builder()
                            .sendTo(memberEmail)
                            .bcc(null)
                            .cc(null)
                            .templateId(orderMaterialTemplateId.get().getConfigValue())
                            .params(Map.of("ASSIGNED_BY", userInfoService.getUserInfo(member.getAccountId()).getFullName(), "TASK", sprint.getSprintName()))
                            .build();
                    sendEmailProducer.sendMessage(sendEmailEvent);
                }
            }
            for (MemberInfo member : managers) {
                handleNotifyProducer.sendMessage(HandleNotifyEvent.builder()
                        .accountId(member.getAccountId())
                        .content(userInfoService.getUserInfo(member.getAccountId()).getFullName() + " updated sprint \"" + sprint.getSprintName() + "\"")
                        .createdDate(LocalDateTime.now())
                        .build());
            }
        }
    }

    @Override
    public void deleteSprint(String workspaceId, String sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint id not found"));
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace id not found"));

        List<Sprint> sprints = workspace.getSprints();
        if (sprints.stream().noneMatch(m -> m.getSprintId().equals(sprintId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint not exist in Workspace");
        }
        List<Task> taskList = sprint.getTasks();
        taskList.stream().map(Task::getTaskId).forEach((taskId) -> taskService.deleteTask(sprintId, taskId));
        sprints.removeIf(m -> m.getSprintId().equals(sprintId));
        workspace.setSprints(sprints);
        try {
            sprintRepository.delete(sprint);
            log.info("Delete sprint success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete sprint in database  " + ex.getMessage());
        }
        try {
            workspaceRepository.save(workspace);
            log.info("Save workspace success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save workspace in database  " + ex.getMessage());
        }
    }

    @Override
    public GetSprintContainerResponse getSprint(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<MemberInfo> memberInfos = workspace.getMembers();
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).get();
        MemberInfo memberInfo = memberInfos.stream().filter(v -> v.getAccountId().equals(accountId)).findFirst().orElseThrow(
                () -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account Id not exist in workspace")
        );
        List<Sprint> sprints = workspace.getSprints();
        List<GetSprintResponse> getSprintResponses = sprints.stream().map(this::convertSprintToGetSprintResponse).collect(Collectors.toList());
        return GetSprintContainerResponse.builder()
                .memberId(memberInfo.getMemberId())
                .sprints(new PageableResponse<>(getSprintResponses))
                .build();
    }

    private GetSprintResponse convertSprintToGetSprintResponse(Sprint sprint) {
        List<Task> tasks = sprint.getTasks();
        Integer totalNotStartedTask = (int) tasks.stream().filter(m -> m.getStatus().equals(WorkflowStatusEnum.TO_DO)).count();
        Integer totalInProgressTask = (int) tasks.stream().filter(m -> m.getStatus().equals(WorkflowStatusEnum.IN_PROGRESS)).count();
        Integer totalDoneTask = (int) tasks.stream().filter(m -> m.getStatus().equals(WorkflowStatusEnum.DONE)).count();
        return GetSprintResponse.builder()
                .sprintId(sprint.getSprintId())
                .sprintName(sprint.getSprintName())
                .goal(sprint.getGoal())
                .status(sprint.getStatus())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .totalNotStartedTask(totalNotStartedTask)
                .totalInProgressTask(totalInProgressTask)
                .totalDoneTask(totalDoneTask)
                .tasks(sprint.getTasks().stream().map(this::convertTaskToGetTaskResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    public GetSprintDetailResponse getSprintDetail(String sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint ID not exist"));

        List<Task> tasks = sprint.getTasks();
        Integer totalNotStartedTask = (int) tasks.stream().filter(m -> m.getStatus().equals(WorkflowStatusEnum.TO_DO)).count();
        Integer totalInProgressTask = (int) tasks.stream().filter(m -> m.getStatus().equals(WorkflowStatusEnum.IN_PROGRESS)).count();
        Integer totalDoneTask = (int) tasks.stream().filter(m -> m.getStatus().equals(WorkflowStatusEnum.DONE)).count();

        List<GetTaskResponse> getTaskResponses = sprint.getTasks().stream().map(this::convertTaskToGetTaskResponse).collect(Collectors.toList());
        return GetSprintDetailResponse.builder()
                .sprintId(sprintId)
                .sprintName(sprint.getSprintName())
                .status(sprint.getStatus())
                .goal(sprint.getGoal())
                .startDate(sprint.getStartDate())
                .dueDate(sprint.getEndDate())
                .tasks(getTaskResponses)
                .totalNotStartedTask(totalNotStartedTask)
                .totalInProgressTask(totalInProgressTask)
                .totalDoneTask(totalDoneTask)
                .build();
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
                        .userInfo(userInfoService.getUserInfo(assignee.getAccountId()))
                        .build())
                .build();
    }
}
