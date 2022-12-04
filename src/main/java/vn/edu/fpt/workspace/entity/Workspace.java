package vn.edu.fpt.workspace.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.workspace.entity.common.Auditor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 21:36
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "workspaces")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Workspace extends Auditor {

    private static final long serialVersionUID = -5554748384583398960L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String workspaceId;
    @Field(name = "sprint_duration")
    @Builder.Default
    private Integer sprintDuration = 14;
    @Field(name = "sprints")
    @DBRef(lazy = true)
    @Builder.Default
    private List<Sprint> sprints = new ArrayList<>();
    @Field(name = "members")
    @DBRef(lazy = true)
    @Builder.Default
    private List<MemberInfo> members = new ArrayList<>();
}
