package vn.edu.fpt.workspace.dto.request.subtask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CreateSubTaskRequest implements Serializable {

    private static final long serialVersionUID = -3724319204047851627L;
    private String memberId;
    private String subTaskName;
}
