package vn.edu.fpt.workspace.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.workspace.entity.common.Auditor;

import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:23
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "stories")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class Story extends Auditor {

    private static final long serialVersionUID = -2449558145259896156L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String storyId;
    @Field(name = "key")
    private String key;
    @Field(name = "title")
    private String title;
    @Field(name = "attachment")
    private String attachment;
    @Field(name = "link_issue")
    private String linkIssue;
    @Field(name = "status")
    private String status;
    @Field(name = "description")
    private String description;
    @Field(name = "tasks")
    private List<Task> tasks;
    @Field(name = "assignee")
    private UserInfo assignee;
    @Field(name = "label")
    private Integer label;
    @Field(name = "sprint")
    private String sprint;
    @Field(name = "estimate")
    private Integer estimate;
    @Field(name = "reporter")
    private UserInfo reporter;
    @Field(name = "activities")
    private List<Activity> activities;
}
