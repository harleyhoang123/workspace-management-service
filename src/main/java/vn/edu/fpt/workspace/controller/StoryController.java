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
import vn.edu.fpt.workspace.dto.response.story.CreateStoryResponse;
import vn.edu.fpt.workspace.dto.response.story.GetStoryDetailResponse;
import vn.edu.fpt.workspace.dto.response.story.GetStoryResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:39
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/stories")
public interface StoryController {

    ResponseEntity<GeneralResponse<CreateStoryResponse>> createStory(@PathVariable(name = "workspace-id")String workspaceId,@RequestBody CreateStoryRequest request);

    ResponseEntity<GeneralResponse<Object>> updateStory(@PathVariable(name = "story-id") String storyId, @RequestBody UpdateStoryRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteStory(@PathVariable(name = "story-id") String storyId);

    ResponseEntity<GeneralResponse<PageableResponse<GetStoryResponse>>> getStory(
            @RequestParam(name = "project-id") String projectId,
            @RequestParam(name = "status", required = false) String status
    );

    ResponseEntity<GeneralResponse<GetStoryDetailResponse>> getStoryDetail(@PathVariable(name = "story-id") String storyId);
}
