package vn.edu.fpt.workspace.dto.response.story;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.config.datetime.CustomDateTimeDeserializer;
import vn.edu.fpt.workspace.constant.SprintStatusEnum;
import vn.edu.fpt.workspace.dto.response.task.GetTaskResponse;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:07
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetSprintResponse implements Serializable {

    private static final long serialVersionUID = 3071771222131304649L;
    private String sprintId;
    private String sprintName;
    private String goal;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime startDate;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime endDate;
    private SprintStatusEnum status;
    private List<GetTaskResponse> tasks;
}
