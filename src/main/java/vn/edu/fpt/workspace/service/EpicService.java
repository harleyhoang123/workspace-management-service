package vn.edu.fpt.workspace.service;

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
 * @created : 08/11/2022 - 09:34
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface EpicService {

    CreateEpicResponse createEpic(CreateEpicRequest request);

    void updateEpic(String epicId, UpdateEpicRequest request);

    void deleteEpic(String epicId);

    PageableResponse<GetEpicResponse> getEpic(String projectId, String status);

    GetEpicDetailResponse getEpicDetail(String epicId);
}
