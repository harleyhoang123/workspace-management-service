package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.sprint.CreateSprintRequest;
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
@RequestMapping("${app.application-context}/public/api/v1/stories")
public interface SprintController {

    ResponseEntity<GeneralResponse<CreateSprintResponse>> createSprint(@PathVariable(name = "workspace-id")String workspaceId, @RequestBody CreateSprintRequest request);

    ResponseEntity<GeneralResponse<Object>> updateSprint(@PathVariable(name = "sprint-id") String sprintId, @RequestBody UpdateSprintRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteSprint(@PathVariable(name = "sprint-id") String sprintId);

    ResponseEntity<GeneralResponse<PageableResponse<GetSprintResponse>>> getSprint(
            @RequestParam(name = "project-id") String projectId,
            @RequestParam(name = "status", required = false) String status
    );

    ResponseEntity<GeneralResponse<GetSprintDetailResponse>> getSprintDetail(@PathVariable(name = "sprint-id") String sprintId);
}
