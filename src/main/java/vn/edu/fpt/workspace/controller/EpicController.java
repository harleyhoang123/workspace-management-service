package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.epic.CreateEpicRequest;
import vn.edu.fpt.workspace.dto.request.epic.UpdateEpicRequest;
import vn.edu.fpt.workspace.dto.response.epic.CreateEpicResponse;
import vn.edu.fpt.workspace.dto.response.epic.GetEpicDetailResponse;
import vn.edu.fpt.workspace.dto.response.epic.GetEpicResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:39
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/epics")
public interface EpicController {

    ResponseEntity<GeneralResponse<CreateEpicResponse>> createEpic(@RequestBody CreateEpicRequest request);

    ResponseEntity<GeneralResponse<Object>> updateEpic(@PathVariable(name = "epic-id") String epicId, @RequestBody UpdateEpicRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteEpic(@PathVariable(name = "epic-id") String epicId);

    ResponseEntity<GeneralResponse<PageableResponse<GetEpicResponse>>> getEpic(
            @RequestParam(name = "project-id") String projectId,
            @RequestParam(name = "status", required = false) String status
    );

    ResponseEntity<GeneralResponse<GetEpicDetailResponse>> getEpicDetail(@PathVariable(name = "epic-id") String epicId);

}
