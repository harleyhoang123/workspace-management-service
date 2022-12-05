package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.sprint.CreateSprintRequest;
import vn.edu.fpt.workspace.dto.request.sprint.GetSprintContainerResponse;
import vn.edu.fpt.workspace.dto.request.sprint.UpdateSprintRequest;
import vn.edu.fpt.workspace.dto.response.sprint.CreateSprintResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintDetailResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:39
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/sprints")
public interface SprintController {
    @PostMapping("/{workspace-id}/sprint")
    ResponseEntity<GeneralResponse<CreateSprintResponse>> createSprint(@PathVariable(name = "workspace-id")String workspaceId, @RequestBody CreateSprintRequest request);

    @PutMapping("/{sprint-id}")
    ResponseEntity<GeneralResponse<Object>> updateSprint(@PathVariable(name = "sprint-id") String sprintId, @RequestBody UpdateSprintRequest request);

    @DeleteMapping("/{workspace-id}/sprints/{sprint-id}")
    ResponseEntity<GeneralResponse<Object>> deleteSprint(@PathVariable(name = "workspace-id") String workspaceId, @PathVariable(name = "sprint-id") String sprintId);

    @GetMapping("/{workspace-id}/sprints")
    ResponseEntity<GeneralResponse<GetSprintContainerResponse>> getSprint(
            @PathVariable(name = "workspace-id") String workspaceId
    );

    @GetMapping("/{sprint-id}")
    ResponseEntity<GeneralResponse<GetSprintDetailResponse>> getSprintDetail(@PathVariable(name = "sprint-id") String sprintId);
}
