package vn.edu.fpt.workspace.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
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
    @Field(name = "story_name")
    private String storyName;
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
    @Field(name = "assignee")
    @DBRef(lazy = true)
    private UserInfo assignee;
    @Field(name = "label")
    private Integer label;
    @Field(name = "sprint")
    private String sprint;
    @Field(name = "estimate")
    private Integer estimate;
    @Field(name = "reporter")
    private UserInfo reporter;
    @Field(name = "start_date")
    private String startDate;
    @Field(name = "due_date")
    private String dueDate;
    @Field(name = "tasks")
    @Builder.Default
    @DBRef(lazy = true)
    private List<Task> tasks = new ArrayList<>();
    @Field(name = "activities")
    @Builder.Default
    @DBRef(lazy = true)
    private List<Activity> activities = new ArrayList<>();
}
