package vn.edu.fpt.workspace.dto.request.subtask;

import com.amazonaws.services.glue.model.Workflow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.entity.MemberInfo;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 30/11/2022 - 20:05
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateSubTaskRequest implements Serializable {

    private static final long serialVersionUID = -3396045946645022218L;
    private String subTaskName;
    private String description;
    private String status;
    private String assignee;
    private Integer label;
    private Integer estimate;
    private String reporter;
}
