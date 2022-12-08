package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.response.workspace.GetMemberInWorkspaceResponse;
import vn.edu.fpt.workspace.dto.response.workspace._CreateWorkspaceResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 21:26
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/workspaces")
public interface WorkspaceController {

    @PostMapping("/{project-id}")
    ResponseEntity<GeneralResponse<_CreateWorkspaceResponse>> createWorkspace(@PathVariable(name = "project-id") String projectId);
    @GetMapping("/{workspace-id}")
    ResponseEntity<GeneralResponse<PageableResponse<GetMemberInWorkspaceResponse>>> getMemberInWorkspace(
            @PathVariable(name = "workspace-id") String workspaceId
    );
}
