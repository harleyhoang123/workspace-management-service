package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
 * @created : 05/11/2022 - 14:41
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/dashboard")
public interface DashboardController {
    ResponseEntity<GeneralResponse<CreateSprintResponse>> createDashBoard(@RequestBody CreateSprintRequest request);

    ResponseEntity<GeneralResponse<Object>> updateDashBoard(@PathVariable(name = "story-id") String storyId, @RequestBody UpdateSprintRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteDashBoard(@PathVariable(name = "story-id") String storyId);

    ResponseEntity<GeneralResponse<PageableResponse<GetSprintResponse>>> getDashBord(
            @RequestParam(name = "project-id") String projectId,
            @RequestParam(name = "status", required = false) String status
    );

    ResponseEntity<GeneralResponse<GetSprintDetailResponse>> getDashBoardDetails(@PathVariable(name = "story-id") String storyId);
}
