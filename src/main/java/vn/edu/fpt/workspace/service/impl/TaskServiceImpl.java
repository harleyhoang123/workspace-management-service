package vn.edu.fpt.workspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.constant.ActivityTypeEnum;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.constant.WorkSpaceRoleEnum;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.task.CreateTaskRequest;
import vn.edu.fpt.workspace.dto.request.task.UpdateTaskRequest;
import vn.edu.fpt.workspace.dto.response.task.CreateTaskResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskDetailResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;
import vn.edu.fpt.workspace.entity.*;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.repository.ActivityRepository;
import vn.edu.fpt.workspace.repository.SprintRepository;
import vn.edu.fpt.workspace.repository.TaskRepository;
import vn.edu.fpt.workspace.service.TaskService;

import java.util.List;

/**
 * vn.edu.fpt.workspace.service.impl
 *
 * @author : Portgas.D.Ace
 * @created : 04/12/2022
 * @contact : 0339850697- congdung2510@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final ActivityRepository activityRepository;
    @Override
    public CreateTaskResponse createTask(String storiesId, CreateTaskRequest request) {
        Sprint sprint = sprintRepository.findById(storiesId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Stories ID not exist"));

        String memberId = request.getMemberId();
        MemberInfo memberInfo = story.getMembers().stream().filter(m -> m.getMemberId().equals(memberId)).findAny()
                .orElseThrow(() -> new BusinessException("Member ID not contain in repository member"));


        Activity activity = Activity.builder()
                .memberInfo(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("created the Issue")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        }catch (Exception ex){
            throw new BusinessException("Can't save activity in database: "+ ex.getMessage());
        }
        Task task = Task.builder()
                .taskName(request.getTaskName())
                .activities(List.of(activity))
                .build();
        try {
            task = taskRepository.save(task);
            log.info("Create task success");
        }catch (Exception ex){
            throw new BusinessException("Can't save new task to database: "+ ex.getMessage());
        }
        List<Task> currentTask = story.getTasks();
        currentTask.add(task);
        story.setTasks(currentTask);
        try {
            sprintRepository.save(story);
            log.info("Update story success");
        }catch (Exception ex){
            throw new BusinessException("Can't update story in database: "+ ex.getMessage());
        }
        return CreateTaskResponse.builder()
                .taskId(task.getTaskId())
                .build();
    }

    @Override
    public void updateTask(String taskId, UpdateTaskRequest request) {

    }

    @Override
    public void deleteTask(String taskId) {

    }

    @Override
    public PageableResponse<GetTaskResponse> getTask(String projectId, String status) {
        return null;
    }

    @Override
    public GetTaskDetailResponse getTaskDetail(String taskId) {
        return null;
    }
}
