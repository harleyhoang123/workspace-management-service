package vn.edu.fpt.workspace.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.workspace.constant.ActivityType;

import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:24
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Activity {

    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String activityId;
    @Field(name = "type", targetType = FieldType.STRING)
    private ActivityType type;
    @Field(name = "user_info")
    private UserInfo userInfo;
    @Field(name = "changed_data")
    private String changedData;
    @Field(name = "changed_date")
    private LocalDateTime changedDate;
}
