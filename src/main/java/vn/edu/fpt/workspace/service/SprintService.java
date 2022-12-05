package vn.edu.fpt.workspace.service;
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
 * @created : 08/11/2022 - 09:34
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface SprintService {

    CreateSprintResponse createSprint(String workspaceId, CreateSprintRequest request);

    void updateSprint(String sprintId, UpdateSprintRequest request);

    void deleteSprint(String workspaceId, String sprintId);

    PageableResponse<GetSprintResponse> getSprint(String workspaceId);

    GetSprintDetailResponse getSprintDetail(String sprintId);
}
