package vn.edu.fpt.workspace.dto.response.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.response.subtask.GetSubTaskResponse;
import vn.edu.fpt.workspace.entity.MemberInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 30/11/2022 - 20:08
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetTaskDetailResponse implements Serializable {

    private static final long serialVersionUID = 5391171729267233403L;

    private String taskId;
    private String taskName;
    private WorkflowStatusEnum status;
    private String description;
    private List<GetSubTaskResponse> subTasks;
    private MemberInfo assignee;
    private Integer label;
    //private String sprint;
    private MemberInfo reporter;
    private List<ActivityResponse> activityResponses;
}
