package vn.edu.fpt.workspace.dto.response.subtask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;
import vn.edu.fpt.workspace.entity.MemberInfo;

import java.io.Serializable;

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
public class GetSubTaskResponse implements Serializable {

    private static final long serialVersionUID = 4998219774006325783L;
    private String subTaskId;
    private String subTaskName;
    private Integer estimate;
    private UserInfoResponse assignee;
    private WorkflowStatusEnum status;
}
