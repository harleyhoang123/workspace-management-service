package vn.edu.fpt.workspace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.workspace.dto.common.GeneralResponse;
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
 * @created : 05/11/2022 - 14:40
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/roadmaps")
public interface RoadmapController {

    ResponseEntity<GeneralResponse<CreateRoadmapResponse>> createRoadmap(@RequestBody CreateRoadmapRequest request);

    ResponseEntity<GeneralResponse<Object>> updateRoadmap(@PathVariable(name = "roadmap-id") String roadmapId, @RequestBody UpdateRoadmapRequest request);

    ResponseEntity<GeneralResponse<Object>> deleteRoadmap(@PathVariable(name = "roadmap-id") String roadmapId);

    ResponseEntity<GeneralResponse<PageableResponse<GetRoadmapResponse>>> getRoadmap(
            @RequestParam(name = "project-id") String projectId,
            @RequestParam(name = "status", required = false) String status
    );

    ResponseEntity<GeneralResponse<GetRoadmapDetailResponse>> getRoadmapDetail(@PathVariable(name = "roadmap-id") String roadmapId);
}
