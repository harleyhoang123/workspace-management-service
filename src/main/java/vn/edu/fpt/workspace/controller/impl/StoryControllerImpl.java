package vn.edu.fpt.workspace.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.workspace.controller.StoryController;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.story.CreateStoryRequest;
import vn.edu.fpt.workspace.dto.request.story.UpdateStoryRequest;
import vn.edu.fpt.workspace.dto.response.story.CreateStoryResponse;
import vn.edu.fpt.workspace.dto.response.story.GetStoryDetailResponse;
import vn.edu.fpt.workspace.dto.response.story.GetStoryResponse;
import vn.edu.fpt.workspace.factory.ResponseFactory;
import vn.edu.fpt.workspace.service.StoryService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:42
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class StoryControllerImpl implements StoryController {

    private final StoryService storyService;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<GeneralResponse<CreateStoryResponse>> createStory(String workspaceId, CreateStoryRequest request) {
        return responseFactory.response(storyService.createStory(workspaceId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateStory(String storyId, UpdateStoryRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteStory(String storyId) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetStoryResponse>>> getStory(String projectId, String status) {
        return null;
    }

    @Override
    public ResponseEntity<GeneralResponse<GetStoryDetailResponse>> getStoryDetail(String storyId) {
        return null;
    }
}
