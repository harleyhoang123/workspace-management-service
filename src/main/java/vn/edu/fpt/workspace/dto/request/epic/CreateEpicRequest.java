package vn.edu.fpt.workspace.dto.request.epic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:53
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateEpicRequest implements Serializable {

    private static final long serialVersionUID = -7477059896170173029L;
    private String epicName;
    private String status;
    private String description;
    private String assignee;
    private String label;
    private String startDate;
    private String dueDate;
    private String reporter;
}
