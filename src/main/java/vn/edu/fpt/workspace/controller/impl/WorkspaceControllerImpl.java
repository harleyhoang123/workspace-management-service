package vn.edu.fpt.workspace.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.workspace.controller.WorkspaceController;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.response.workspace.GetWorkspaceDetailResponse;
import vn.edu.fpt.workspace.dto.response.workspace._CreateWorkspaceResponse;
import vn.edu.fpt.workspace.factory.ResponseFactory;
import vn.edu.fpt.workspace.service.WorkspaceService;

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
    public ResponseEntity<GeneralResponse<_CreateWorkspaceResponse>> createWorkspace(String labId) {
        return responseFactory.response(workspaceService.createWorkspace(labId));
    }

    @Override
    public ResponseEntity<GeneralResponse<GetWorkspaceDetailResponse>> getWorkspaceDetail(String workspaceId) {
        return responseFactory.response(workspaceService.getWorkspaceByWorkSpaceId(workspaceId));
    }
}