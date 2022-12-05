package vn.edu.fpt.workspace.dto.response.sprint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import vn.edu.fpt.workspace.config.datetime.CustomDateTimeDeserializer;
import vn.edu.fpt.workspace.constant.SprintStatusEnum;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.AuditableResponse;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;
import vn.edu.fpt.workspace.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 30/11/2022 - 19:58
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GetSprintDetailResponse extends AuditableResponse {

    private static final long serialVersionUID = 300956349883218180L;

    private String sprintId;
    private String sprintName;
    private SprintStatusEnum status;
    private String goal;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime startDate;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime dueDate;
    private List<GetTaskResponse> tasks;
    private Integer totalNotStartedTask;
    private Integer totalInProgressTask;
    private Integer totalDoneTask;
}
