package vn.edu.fpt.workspace.dto.request.sprint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.dto.common.PageableResponse;
import vn.edu.fpt.workspace.dto.response.sprint.GetSprintResponse;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/12/2022 - 22:32
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetSprintContainerResponse implements Serializable {

    private static final long serialVersionUID = -1129116140191425470L;
    private String memberId;
    private PageableResponse<GetSprintResponse> sprints;
}
