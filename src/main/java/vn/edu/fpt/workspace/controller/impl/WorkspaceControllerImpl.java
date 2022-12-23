package vn.edu.fpt.workspace.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.workspace.controller.WorkspaceController;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.common.SortableRequest;
import vn.edu.fpt.workspace.dto.event.GenerateProjectAppEvent;
import vn.edu.fpt.workspace.dto.request.workspace.GetActivityStreamRequest;
import vn.edu.fpt.workspace.dto.request.workspace.GetAssignToMeRequest;
import vn.edu.fpt.workspace.dto.response.workspace.*;
import vn.edu.fpt.workspace.factory.ResponseFactory;
import vn.edu.fpt.workspace.service.WorkspaceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 21:27
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkspaceControllerImpl implements WorkspaceController {

    private final ResponseFactory responseFactory;
    private final WorkspaceService workspaceService;

    @Override
    public ResponseEntity<GeneralResponse<_CreateWorkspaceResponse>> createWorkspace(String projectId) {

        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).get();
        return responseFactory.response(workspaceService.createWorkspace(GenerateProjectAppEvent.builder()
                .accountId(accountId)
                .projectId(projectId)
                .build()));
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetMemberInWorkspaceResponse>>> getMemberInWorkspace(String projectId) {
        return responseFactory.response(workspaceService.getMemberInWorkspace(projectId));
    }

    @Override
    public ResponseEntity<GeneralResponse<GetIssueStaticResponse>> getIssueStatic(String workspaceId) {
        return responseFactory.response(workspaceService.getIssueStaticDashboard(workspaceId));
    }

    @Override
    public ResponseEntity<GeneralResponse<List<GetAssignedToMeResponse>>> getAssignToMe(String workspaceId, String memberId) {
        return responseFactory.response(workspaceService.getAssignedToMeDashboard(workspaceId, memberId));
    }

    @Override
    public ResponseEntity<GeneralResponse<GetActivityStreamResponse>> getActivitySteam(String workspaceId, String createdDateSortBy) {
        List<SortableRequest> sortableRequests = new ArrayList<>();
        if(Objects.nonNull(createdDateSortBy)){
            sortableRequests.add(new SortableRequest("created_date", createdDateSortBy));
        }
        GetActivityStreamRequest request = GetActivityStreamRequest.builder()
                .sortBy(sortableRequests)
                .build();
        return responseFactory.response(workspaceService.getActivityStreamDashboard(workspaceId, request));
    }
}
