package vn.edu.fpt.workspace.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.workspace.constant.ProjectStatus;
import vn.edu.fpt.workspace.dto.cache.UserInfo;
import vn.edu.fpt.workspace.entity.common.Auditor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:23
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "epics")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class Epic extends Auditor {

    private static final long serialVersionUID = -1727997711195086418L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String epicId;
    @Field(name = "key")
    private String key;
    @Field(name = "title")
    private String title;
    @Field(name = "attachment")
    private String attachment;
    @Field(name = "link_issue")
    private String linkIssue;
    @Field(name = "status", targetType = FieldType.STRING)
    private ProjectStatus status;
    @Field(name = "description")
    private String description;
    @Field(name = "assignee")
    private String assignee;
    @Field(name = "label")
    private String label;
    @Field(name = "start_date")
    private LocalDateTime startDate;
    @Field(name = "due_date")
    private LocalDateTime dueDate;
    @Field(name = "reporter")
    private UserInfo reporter;
    @Field(name = "activities")
    private List<Activity> activities;
}
