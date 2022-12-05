package vn.edu.fpt.workspace.dto.response.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.entity.MemberInfo;

import java.io.Serializable;

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
public class GetTaskResponse implements Serializable {

    private static final long serialVersionUID = -3205928614165623152L;
    private String taskId;
    private String taskName;
    private Integer estimate;
    private WorkflowStatusEnum status;
    private MemberInfo assignee;

}
