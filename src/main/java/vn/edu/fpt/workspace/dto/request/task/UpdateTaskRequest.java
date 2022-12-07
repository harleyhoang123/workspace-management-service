package vn.edu.fpt.workspace.dto.request.task;

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
 * @created : 30/11/2022 - 20:09
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateTaskRequest implements Serializable {

    private static final long serialVersionUID = 6918033186813123871L;
    private String taskName;
    private String status;
    private String description;
    private String assignee;
    private Integer label;
    private Integer estimate;
    private String reporter;

}
