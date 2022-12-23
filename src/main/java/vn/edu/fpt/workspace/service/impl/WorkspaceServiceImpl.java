package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;
import vn.edu.fpt.workspace.dto.event.ModifyMembersToWorkspaceEvent;
import vn.edu.fpt.workspace.dto.event.GenerateProjectAppEvent;
import vn.edu.fpt.workspace.dto.request.workspace.GetActivityStreamRequest;
import vn.edu.fpt.workspace.dto.response.workspace.*;
import vn.edu.fpt.workspace.entity.*;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.BaseMongoRepository;
import vn.edu.fpt.workspace.repository.MemberInfoRepository;
import vn.edu.fpt.workspace.repository.WorkspaceRepository;
import vn.edu.fpt.workspace.service.UserInfoService;
import vn.edu.fpt.workspace.service.WorkspaceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 22:13
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MongoTemplate mongoTemplate;
    private final UserInfoService userInfoService;


    @Override
    public _CreateWorkspaceResponse createWorkspace(GenerateProjectAppEvent event) {
        MemberInfo memberInfo = MemberInfo.builder()
                .accountId(event.getAccountId())
                .role(WorkSpaceRoleEnum.OWNER.getRole())
                .build();
        try {
            memberInfo = memberInfoRepository.save(memberInfo);
            log.info("Create member info when create work space success: {}", memberInfo);
        } catch (Exception ex) {
            throw new BusinessException("Can't save member info to database: " + ex.getMessage());
        }

        Workspace workSpace = Workspace.builder()
                .workspaceId(event.getProjectId())
                .members(List.of(memberInfo))
                .build();
        try {
            workSpace = workspaceRepository.save(workSpace);
            log.info("Create workspace success: {}", workSpace);
        } catch (Exception ex) {
            throw new BusinessException("Can't save workspace to database: " + ex.getMessage());
        }
        return _CreateWorkspaceResponse.builder()
                .workspaceId(workSpace.getWorkspaceId())
                .memberId(memberInfo.getMemberId())
                .build();
    }

    @Override
    public void modifyMembersToWorkspace(ModifyMembersToWorkspaceEvent event) {
        Workspace workspace = workspaceRepository.findById(event.getWorkspaceId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<MemberInfo> memberInfos = workspace.getMembers();
        if (event.getType().equals("ADD")) {
            if (memberInfos.stream().anyMatch(m -> m.getAccountId().equals(event.getAccountId()))) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID is already exist in Workspace");
            }
            MemberInfo memberInfo = MemberInfo.builder()
                    .accountId(event.getAccountId())
                    .role(WorkSpaceRoleEnum.MEMBER.getRole())
                    .build();
            try {
                memberInfo = memberInfoRepository.save(memberInfo);
                log.info("Create memberInfo success: {}", memberInfo);
            } catch (Exception ex) {
                throw new BusinessException("Can't create memberInfo to database: " + ex.getMessage());
            }

            memberInfos.add(memberInfo);
            workspace.setMembers(memberInfos);
            try {
                workspaceRepository.save(workspace);
                log.info("Add member to workspace success: {}", memberInfo);
            } catch (Exception ex) {
                throw new BusinessException("Can't add member to workspace in database: " + ex.getMessage());
            }
        } else {
            MemberInfo memberInfo = memberInfos.stream().filter(m -> m.getAccountId().equals(event.getAccountId())).findFirst().get();
            if (memberInfo == null) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID is not exist in Workspace");
            }
            memberInfos.removeIf(m -> m.getAccountId().equals(event.getAccountId()));
            workspace.setMembers(memberInfos);
            try {
                workspaceRepository.save(workspace);
                log.info("Delete member from workspace success: {}", memberInfo);
            } catch (Exception ex) {
                throw new BusinessException("Can't delete member from workspace: " + ex.getMessage());
            }

            try {
                memberInfoRepository.deleteById(memberInfo.getAccountId());
                log.info("Delete member in database success: {}", memberInfo);
            } catch (Exception ex) {
                throw new BusinessException("Can't delete member in database: " + ex.getMessage());
            }
        }

    }

    @Override
    public PageableResponse<GetMemberInWorkspaceResponse> getMemberInWorkspace(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint ID not exist"));
        List<MemberInfo> memberInfos = workspace.getMembers();
        List<GetMemberInWorkspaceResponse> getMemberInWorkspaceResponses = memberInfos.stream().map(this::convertMemberInfoToGetMemberInWorkspaceResponse).collect(Collectors.toList());

        return new PageableResponse<>(getMemberInWorkspaceResponses);
    }

    private GetMemberInWorkspaceResponse convertMemberInfoToGetMemberInWorkspaceResponse(MemberInfo memberInfo) {
        return GetMemberInWorkspaceResponse.builder()
                .memberId(memberInfo.getMemberId())
                .userInfo(ConvertMemberInfoToUserInfoResponse(memberInfo))
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

    @Override
    public GetIssueStaticResponse getIssueStaticDashboard(String workspaceId) {
        if (!ObjectId.isValid(workspaceId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID invalid");
        }
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));

        List<MemberInfo> memberInfos = workspace.getMembers();
        List<Sprint> sprints = workspace.getSprints();
        List<Task> listAllTask = new ArrayList<>();
        sprints.stream().forEach(s -> listAllTask.addAll(s.getTasks()));
        List<SubTask> listAllSubTask = new ArrayList<>();
        listAllTask.stream().forEach(t -> listAllSubTask.addAll(t.getSubTasks()));
        long totalTask = listAllTask.stream().count() + listAllSubTask.stream().count();
        HashMap<UserInfoResponse, Long> issueStatic = new HashMap<>();

        for (MemberInfo m : memberInfos) {
            long count = 0;
            for (Task t : listAllTask) {
                if (Objects.nonNull(t.getAssignee())) {
                    if (m.getMemberId().equals(t.getAssignee().getMemberId())) {
                        count++;
                    }
                }
            }
            for (SubTask s : listAllSubTask) {
                if (Objects.nonNull(s.getAssignee())) {
                    if (m.getMemberId().equals(s.getAssignee().getMemberId())) {
                        count++;
                    }
                }
            }
            issueStatic.put(convertMemberInfoToUserInfoResponse(m), count);
        }
        AtomicLong totalUnassigned = new AtomicLong(totalTask);
        issueStatic.forEach((key, value) -> totalUnassigned.set(totalUnassigned.get() - value));
        return GetIssueStaticResponse.builder()
                .totalIssue(totalTask)
                .issueStatic(issueStatic)
                .totalUnassigned(Long.valueOf(totalUnassigned.toString()))
                .build();
    }

    private UserInfoResponse convertMemberInfoToUserInfoResponse(MemberInfo memberInfo) {
        if (Objects.isNull(memberInfo)) {
            return null;
        }
        return UserInfoResponse.builder()
                .memberId(memberInfo.getMemberId())
                .accountId(memberInfo.getAccountId())
                .userInfo(userInfoService.getUserInfo(memberInfo.getAccountId()))
                .build();
    }

    @Override
    public List<GetAssignedToMeResponse> getAssignedToMeDashboard(String workspaceId, String memberInfoId) {
        if (!ObjectId.isValid(workspaceId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID invalid");
        }
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));

        List<Sprint> sprints = workspace.getSprints();
        List<Task> listAllTask = new ArrayList<>();
        sprints.stream().forEach(s -> listAllTask.addAll(s.getTasks()));
        List<SubTask> listAllSubTask = new ArrayList<>();
        listAllTask.stream().forEach(t -> listAllSubTask.addAll(t.getSubTasks()));

        List<GetAssignedToMeResponse> responses = new ArrayList<>();


        for (Task t : listAllTask) {
            if (Objects.nonNull(t.getAssignee())) {
                if (t.getAssignee().getMemberId().equals(memberInfoId)) {
                    responses.add(GetAssignedToMeResponse.builder()
                            .issueName(t.getTaskName())
                            .estimate(t.getEstimate())
                            .reporter(convertMemberInfoToUserInfoResponse(t.getReporter()))
                            .build());
                }
            }
        }
        for (SubTask s : listAllSubTask) {
            if (Objects.nonNull(s.getAssignee())) {
                if (s.getAssignee().getMemberId().equals(memberInfoId)) {
                    responses.add(GetAssignedToMeResponse.builder()
                            .issueName(s.getSubtaskName())
                            .estimate(s.getEstimate())
                            .reporter(convertMemberInfoToUserInfoResponse(s.getReporter()))
                            .build());
                }
            }
        }
        return responses;
    }

    @Override
    public GetActivityStreamResponse getActivityStreamDashboard(String workspaceId, GetActivityStreamRequest request) {
        if (!ObjectId.isValid(workspaceId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID invalid");
        }
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        LocalDateTime currentDate = LocalDateTime.now();

        List<Sprint> sprints = workspace.getSprints();
        List<Task> listAllTask = new ArrayList<>();
        sprints.stream().forEach(s -> listAllTask.addAll(s.getTasks()));
        List<Activity> listActivities = new ArrayList<>();
        listAllTask.stream().forEach(t-> listActivities.addAll(t.getActivities()));
        List<SubTask> listAllSubTask = new ArrayList<>();
        listAllTask.stream().forEach(t -> listAllSubTask.addAll(t.getSubTasks()));
        listAllSubTask.stream().forEach(t-> listActivities.addAll(t.getActivities()));
        listActivities.removeIf(v->v.getChangedDate().isBefore(currentDate.minusDays(7)));

        return GetActivityStreamResponse.builder()
                .activities(listActivities.stream().map(this::convertActivityToActivityResponse).collect(Collectors.toList()))
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

