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
 * @created : 05/11/2022 - 14:40
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/backlog")
public interface BacklogController {
    ResponseEntity<GeneralResponse<CreateSprintResponse>> createBackLog(@RequestBody CreateStoryRequest request);

    ResponseEntity<GeneralResponse<Object>> updateBackLog(@PathVariable(name = "backlog-id") String storyId, @RequestBody UpdateStoryRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteBackLog(@PathVariable(name = "backlog-id") String storyId);

    ResponseEntity<GeneralResponse<PageableResponse<GetSprintResponse>>> getBackLog(
            @RequestParam(name = "backlog-id") String backLogId
    );

    ResponseEntity<GeneralResponse<GetStoryDetailResponse>> getBackLogDetails(@PathVariable(name = "backlog-id") String backLogId);
}
