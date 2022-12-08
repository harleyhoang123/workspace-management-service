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
import vn.edu.fpt.workspace.dto.event.CreateWorkspaceEvent;
import vn.edu.fpt.workspace.dto.response.workspace.GetWorkspaceDetailResponse;
import vn.edu.fpt.workspace.dto.response.workspace._CreateWorkspaceResponse;
import vn.edu.fpt.workspace.factory.ResponseFactory;
import vn.edu.fpt.workspace.service.WorkspaceService;

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
        return responseFactory.response(workspaceService.createWorkspace(CreateWorkspaceEvent.builder()
                .accountId(accountId)
                .projectId(projectId)
                .build()));
    }
}
