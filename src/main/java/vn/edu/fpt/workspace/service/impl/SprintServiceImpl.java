package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.constant.ActivityTypeEnum;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.sprint.CreateSprintRequest;
import vn.edu.fpt.workspace.dto.request.sprint.UpdateSprintRequest;
import vn.edu.fpt.workspace.dto.response.sprint.CreateSprintResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintDetailResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintResponse;
import vn.edu.fpt.workspace.entity.Activity;
import vn.edu.fpt.workspace.entity.MemberInfo;
import vn.edu.fpt.workspace.entity.Sprint;
import vn.edu.fpt.workspace.entity.Workspace;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.ActivityRepository;
import vn.edu.fpt.workspace.repository.SprintRepository;
import vn.edu.fpt.workspace.repository.WorkspaceRepository;
import vn.edu.fpt.workspace.service.SprintService;

import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public CreateSprintResponse createSprint(String workspaceId, CreateSprintRequest request) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));


        String memberId = request.getMemberId();
        MemberInfo memberInfo = workspace.getMembers().stream().filter(m -> m.getMemberId().equals(memberId)).findAny()
                .orElseThrow(() -> new BusinessException("Member ID not contain in repository member"));

        if (!memberInfo.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()) && !memberInfo.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole())) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Invalid role for create sprint");
        }

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
        }catch (Exception ex){
            throw new BusinessException("Can't save new sprint to database: "+ ex.getMessage());
        }
        List<Sprint> currentSprint = workspace.getSprints();
        currentSprint.add(sprint);
        workspace.setSprints(currentSprint);
        try {
            workspaceRepository.save(workspace);
            log.info("Update workspace success");
        }catch (Exception ex){
            throw new BusinessException("Can't update workspace in database: "+ ex.getMessage());
        }
        return CreateSprintResponse.builder()
                .sprintId(sprint.getSprintId())
                .status(sprint.getStatus())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .build();
    }

    @Override
    public void updateSprint(String sprintId, UpdateSprintRequest request) {

    }

    @Override
    public void deleteSprint(String sprintId) {

    }

    @Override
    public PageableResponse<GetSprintResponse> getSprint(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<Sprint> sprints = workspace.getSprints();
        List<GetSprintResponse> getSprintResponses = sprints.stream().map(this::convertSprintToGetSprintResponse).collect(Collectors.toList());
        return new PageableResponse<>(getSprintResponses);
    }

    private GetSprintResponse convertSprintToGetSprintResponse(Sprint sprint){
        return GetSprintResponse.builder()
                .sprintId(sprint.getSprintId())
                .sprintName(sprint.getSprintName())
                .status(sprint.getStatus())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .build();
    }

    @Override
    public GetSprintDetailResponse getSprintDetail(String sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Sprint ID not exist"));


        return GetSprintDetailResponse.builder()
                .build();
    }
}
