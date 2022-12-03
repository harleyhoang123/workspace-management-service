package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.constant.ActivityType;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.story.CreateStoryRequest;
import vn.edu.fpt.workspace.dto.request.story.UpdateStoryRequest;
import vn.edu.fpt.workspace.dto.response.story.CreateStoryResponse;
import vn.edu.fpt.workspace.dto.response.story.GetStoryDetailResponse;
import vn.edu.fpt.workspace.dto.response.story.GetStoryResponse;
import vn.edu.fpt.workspace.entity.Activity;
import vn.edu.fpt.workspace.entity.MemberInfo;
import vn.edu.fpt.workspace.entity.Story;
import vn.edu.fpt.workspace.entity.Workspace;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.ActivityRepository;
import vn.edu.fpt.workspace.repository.StoryRepository;
import vn.edu.fpt.workspace.repository.WorkspaceRepository;
import vn.edu.fpt.workspace.service.StoryService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 22:47
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ActivityRepository activityRepository;

    @Override
    public CreateStoryResponse createStory(String workspaceId, CreateStoryRequest request) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));


        String memberId = request.getMemberId();
        MemberInfo memberInfo = workspace.getMembers().stream().filter(m -> m.getMemberId().equals(memberId)).findAny()
                .orElseThrow(() -> new BusinessException("Member ID not contain in repository member"));

        if (!memberInfo.getRole().equals(WorkSpaceRoleEnum.OWNER.getRole()) && !memberInfo.getRole().equals(WorkSpaceRoleEnum.MANAGER.getRole())) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Invalid role for create project");
        }

        Activity activity = Activity.builder()
                .memberInfo(memberInfo)
                .type(ActivityType.HISTORY)
                .changedData("created the Issue")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        }catch (Exception ex){
            throw new BusinessException("Can't save activity in database: "+ ex.getMessage());
        }

        Story story = Story.builder()
                .storyName(request.getStoryName())
                .activities(List.of(activity))
                .build();
        try {
            story = storyRepository.save(story);
            log.info("Create story success");
        }catch (Exception ex){
            throw new BusinessException("Can't save new story to database: "+ ex.getMessage());
        }
        List<Story> currentStory = workspace.getStories();
        currentStory.add(story);
        workspace.setStories(currentStory);
        try {
            workspaceRepository.save(workspace);
            log.info("Update workspace success");
        }catch (Exception ex){
            throw new BusinessException("Can't update workspace in database: "+ ex.getMessage());
        }
        return CreateStoryResponse.builder()
                .storyId(story.getStoryId())
                .build();
    }

    @Override
    public void updateStory(String storyId, UpdateStoryRequest request) {

    }

    @Override
    public void deleteStory(String storyId) {

    }

    @Override
    public PageableResponse<GetStoryResponse> getStory(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Workspace ID not exist"));
        List<Story> stories = workspace.getStories();
        List<GetStoryResponse> getStoryResponses = stories.stream().map(this::convertStoryToGetStoryResponse).collect(Collectors.toList());
        return new PageableResponse<>(getStoryResponses);
    }

    private GetStoryResponse convertStoryToGetStoryResponse(Story story){
        return GetStoryResponse.builder()
                .storyId(story.getStoryId())
                .storyName(story.getStoryName())
                .build();
    }

    @Override
    public GetStoryDetailResponse getStoryDetail(String storyId) {
        return null;
    }
}
