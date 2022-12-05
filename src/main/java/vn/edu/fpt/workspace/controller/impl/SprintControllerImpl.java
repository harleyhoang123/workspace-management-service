package vn.edu.fpt.workspace.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.controller.SprintController;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.sprint.CreateSprintRequest;
import vn.edu.fpt.workspace.dto.request.sprint.UpdateSprintRequest;
import vn.edu.fpt.workspace.dto.response.sprint.CreateSprintResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintDetailResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintResponse;
import vn.edu.fpt.workspace.factory.ResponseFactory;
import vn.edu.fpt.workspace.service.SprintService;

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
public class SprintControllerImpl implements SprintController {

    private final SprintService sprintService;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<GeneralResponse<CreateSprintResponse>> createSprint(String workspaceId, CreateSprintRequest request) {
        return responseFactory.response(sprintService.createSprint(workspaceId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateSprint(String sprintId, UpdateSprintRequest request) {
        sprintService.updateSprint(sprintId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteSprint(String sprintId) {
        sprintService.deleteSprint(sprintId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetSprintResponse>>> getSprint(String workspaceId) {
        return responseFactory.response(sprintService.getSprint(workspaceId));
    }

    @Override
    public ResponseEntity<GeneralResponse<GetSprintDetailResponse>> getSprintDetail(String sprintId) {
        return responseFactory.response(sprintService.getSprintDetail(sprintId));
    }
}
