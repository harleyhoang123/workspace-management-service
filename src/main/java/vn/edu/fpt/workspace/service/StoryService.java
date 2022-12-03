package vn.edu.fpt.workspace.service;
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
 * @created : 08/11/2022 - 09:34
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface StoryService {

    CreateStoryResponse createStory(String workspaceId, CreateStoryRequest request);

    void updateStory(String storyId, UpdateStoryRequest request);

    void deleteStory(String storyId);

    PageableResponse<GetStoryResponse> getStory(String workspaceId);

    GetStoryDetailResponse getStoryDetail(String storyId);
}
