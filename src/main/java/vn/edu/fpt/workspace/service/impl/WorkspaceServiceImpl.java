package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;
import vn.edu.fpt.workspace.dto.event.ModifyMembersToWorkspaceEvent;
import vn.edu.fpt.workspace.dto.event.GenerateProjectAppEvent;
import vn.edu.fpt.workspace.dto.response.workspace.GetMemberInWorkspaceResponse;
import vn.edu.fpt.workspace.dto.response.workspace._CreateWorkspaceResponse;
import vn.edu.fpt.workspace.entity.MemberInfo;
import vn.edu.fpt.workspace.entity.Workspace;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.MemberInfoRepository;
import vn.edu.fpt.workspace.repository.WorkspaceRepository;
import vn.edu.fpt.workspace.service.UserInfoService;
import vn.edu.fpt.workspace.service.WorkspaceService;

import java.util.List;
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
            MemberInfo memberInfo = memberInfos.stream().filter(m->m.getAccountId().equals(event.getAccountId())).findFirst().get();
            if (memberInfo == null) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID is not exist in Workspace");
            }
            memberInfos.removeIf(m->m.getAccountId().equals(event.getAccountId()));
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
}

