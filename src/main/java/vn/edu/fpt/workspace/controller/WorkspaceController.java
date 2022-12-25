package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.task.UpdateTaskRequest;
import vn.edu.fpt.workspace.dto.request.workspace.GetAssignToMeRequest;
import vn.edu.fpt.workspace.dto.response.workspace.*;

import java.util.List;

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

    @GetMapping("/{workspace-id}/issue-statistic")
    ResponseEntity<GeneralResponse<GetIssueStaticResponse>> getIssueStatic(@PathVariable(name = "workspace-id") String workspaceId);

    @GetMapping("/{workspace-id}/{member-id}/assign-to-me")
    ResponseEntity<GeneralResponse<List<GetAssignedToMeResponse>>> getAssignToMe(@PathVariable(name = "workspace-id") String workspaceId, @PathVariable(name = "member-id") String memberId);

    @GetMapping("/{workspace-id}/activity-stream")
    ResponseEntity<GeneralResponse<GetActivityStreamResponse>> getActivitySteam(@PathVariable(name = "workspace-id") String workspaceId,
                                                                                @RequestParam(name = "created-date-sort-by", required = false) String createdDateSortBy);
}
