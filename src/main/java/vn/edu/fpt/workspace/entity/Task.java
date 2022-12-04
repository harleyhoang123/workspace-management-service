package vn.edu.fpt.workspace.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.workspace.constant.WorkflowStatusEnum;
import vn.edu.fpt.workspace.dto.cache.UserInfo;
import vn.edu.fpt.workspace.entity.common.Auditor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:23
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class Task extends Auditor {

    private static final long serialVersionUID = 2154638367134242938L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String taskId;
    @Field(name = "task_name")
    private String taskName;
    @Field(name = "status", targetType = FieldType.STRING)
    @Builder.Default
    private WorkflowStatusEnum status = WorkflowStatusEnum.TO_DO;
    @Field(name = "assignee")
    @DBRef(lazy = true)
    private MemberInfo assignee;
    @Field(name = "description")
    private String description;
    @Field(name = "label")
    private Integer label;
    @Field(name = "estimate")
    @Builder.Default
    private Integer estimate = 0;
    @Field(name = "reporter")
    @DBRef(lazy = true)
    private MemberInfo reporter;
    @Field(name = "attachments")
    @DBRef(lazy = true)
    @Builder.Default
    private List<_Attachment> attachments = new ArrayList<>();
    @Field(name = "sub_tasks")
    @DBRef(lazy = true)
    @Builder.Default
    private List<SubTask> subTasks = new ArrayList<>();
    @Field(name = "activities")
    @DBRef(lazy = true)
    @Builder.Default
    private List<Activity> activities = new ArrayList<>();
}
