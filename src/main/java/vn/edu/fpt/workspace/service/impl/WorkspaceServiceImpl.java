package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.dto.response.workspace.GetWorkspaceDetailResponse;
import vn.edu.fpt.workspace.dto.response.workspace._CreateWorkspaceResponse;
import vn.edu.fpt.workspace.entity.MemberInfo;
import vn.edu.fpt.workspace.entity.Workspace;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.MemberInfoRepository;
import vn.edu.fpt.workspace.repository.WorkspaceRepository;
import vn.edu.fpt.workspace.service.UserInfoService;
import vn.edu.fpt.workspace.service.WorkspaceService;

import java.util.List;
import java.util.Optional;

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
    private final MongoTemplate mongoTemplate;

    @Override
    public _CreateWorkspaceResponse createWorkspace(String projectId) {
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername)
                .orElseThrow(() -> new BusinessException("Can't get account id from token"));

        MemberInfo memberInfo = MemberInfo.builder()
                .accountId(accountId)
                .role(WorkSpaceRoleEnum.OWNER.getRole())
                .build();
        try {
            memberInfo = memberInfoRepository.save(memberInfo);
            log.info("Create member info when create work space success: {}", memberInfo);
        }catch (Exception ex){
            throw new BusinessException("Can't save member info to database: "+ ex.getMessage());
        }

        Workspace workSpace = Workspace.builder()
                .workspaceId(projectId)
                .members(List.of(memberInfo))
                .build();
        try {
            workSpace = workspaceRepository.save(workSpace);
            log.info("Create workspace success: {}", workSpace);
        }catch (Exception ex){
            throw new BusinessException("Can't save workspace to database: "+ ex.getMessage());
        }
        return _CreateWorkspaceResponse.builder()
                .workspaceId(workSpace.getWorkspaceId())
                .memberId(memberInfo.getMemberId())
                .build();
    }

    @Override
    public GetWorkspaceDetailResponse getWorkspaceByWorkSpaceId(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));


        return null;
    }
}
