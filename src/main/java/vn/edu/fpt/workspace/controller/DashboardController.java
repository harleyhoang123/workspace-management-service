package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.story.CreateStoryRequest;
import vn.edu.fpt.workspace.dto.request.story.UpdateStoryRequest;
import vn.edu.fpt.workspace.dto.response.story.CreateSprintResponse;
import vn.edu.fpt.workspace.dto.response.story.GetStoryDetailResponse;
import vn.edu.fpt.workspace.dto.response.story.GetSprintResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:41
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/dashboard")
public interface DashboardController {
    ResponseEntity<GeneralResponse<CreateSprintResponse>> createDashBoard(@RequestBody CreateStoryRequest request);

    ResponseEntity<GeneralResponse<Object>> updateDashBoard(@PathVariable(name = "story-id") String storyId, @RequestBody UpdateStoryRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteDashBoard(@PathVariable(name = "story-id") String storyId);

    ResponseEntity<GeneralResponse<PageableResponse<GetSprintResponse>>> getDashBord(
            @RequestParam(name = "project-id") String projectId,
            @RequestParam(name = "status", required = false) String status
    );

    ResponseEntity<GeneralResponse<GetStoryDetailResponse>> getDashBoardDetails(@PathVariable(name = "story-id") String storyId);
}
