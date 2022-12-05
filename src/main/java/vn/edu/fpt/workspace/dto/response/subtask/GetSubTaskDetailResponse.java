package vn.edu.fpt.workspace.dto.response.subtask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.entity.MemberInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 30/11/2022 - 20:04
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetSubTaskDetailResponse implements Serializable {

    private static final long serialVersionUID = -4201112541456118425L;
    private String subTaskId;
    private String subTaskName;
    private String description;
    private WorkflowStatusEnum status;
    private MemberInfo assignee;
    private Integer label;
    private Integer estimate;
    private MemberInfo reporter;
    private List<ActivityResponse> activityResponse;
}
