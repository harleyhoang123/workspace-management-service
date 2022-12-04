package vn.edu.fpt.workspace.dto.response.sprint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.config.datetime.CustomDateTimeDeserializer;
import vn.edu.fpt.workspace.constant.SprintStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 30/11/2022 - 19:58
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateSprintResponse implements Serializable {

    private static final long serialVersionUID = 6647338810156614575L;
    private String sprintId;
    private String sprintName;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime startDate;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime endDate;
    private SprintStatusEnum status;
}
