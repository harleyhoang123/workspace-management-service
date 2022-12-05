package vn.edu.fpt.workspace.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.workspace.constant.SprintStatusEnum;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.entity.common.Auditor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 04/12/2022 - 08:21
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "sprints")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Sprint extends Auditor {

    private static final long serialVersionUID = -5420000939359645975L;
    @Id
    @Field(name = "sprint_id", targetType = FieldType.OBJECT_ID)
    private String sprintId;
    @Field(name = "sprint_name")
    private String sprintName;
    @Field(name = "start_date")
    private LocalDateTime startDate;
    @Field(name = "end_date")
    private LocalDateTime endDate;
    @Field(name = "status", targetType = FieldType.STRING)
    @Builder.Default
    private SprintStatusEnum status = SprintStatusEnum.NOT_START;
    @Field(name = "goal")
    private String goal;
    @Field(name = "tasks")
    @DBRef(lazy = true)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();
    @Field(name = "activities")
    @DBRef(lazy = true)
    @Builder.Default
    private List<Activity> activities = new ArrayList<>();
}
