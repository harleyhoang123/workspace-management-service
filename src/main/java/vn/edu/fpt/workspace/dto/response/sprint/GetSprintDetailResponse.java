package vn.edu.fpt.workspace.dto.response.sprint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import vn.edu.fpt.workspace.config.datetime.CustomDateTimeDeserializer;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.AuditableResponse;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;

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
    private String storyId;
    private String storyName;
    private WorkflowStatusEnum status;
    private String description;
    private UserInfoResponse assignee;
    private Integer label;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime startDate;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime dueDate;
    private UserInfoResponse reporter;
    private List<ActivityResponse> activities;
}
