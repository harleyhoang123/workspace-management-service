package vn.edu.fpt.workspace.service;

import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.request.roadmap.CreateRoadmapRequest;
import vn.edu.fpt.workspace.dto.request.roadmap.UpdateRoadmapRequest;
import vn.edu.fpt.workspace.dto.response.roadmap.CreateRoadmapResponse;
import vn.edu.fpt.workspace.dto.response.roadmap.GetRoadmapDetailResponse;
import vn.edu.fpt.workspace.dto.response.roadmap.GetRoadmapResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 08/11/2022 - 09:35
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface RoadmapService {

    CreateRoadmapResponse createRoadmap(CreateRoadmapRequest request);

    void updateRoadmap(String roadmapId, UpdateRoadmapRequest request);

    void deleteRoadmap(String roadmapId);

    PageableResponse<GetRoadmapResponse> getRoadmap(String projectId, String status);

    GetRoadmapDetailResponse getRoadmapDetail(String roadmapId);
}
