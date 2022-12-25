package vn.edu.fpt.workspace.dto.request.sprint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.constant.SprintStatusEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 30/11/2022 - 19:59
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateSprintRequest implements Serializable {

    private static final long serialVersionUID = 3564693687567387629L;
    private String memberId;
    private String sprintName;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String goal;
    private SprintStatusEnum status;
}
