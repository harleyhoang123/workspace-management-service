package vn.edu.fpt.workspace.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.persistence.EntityListeners;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 04/12/2022 - 09:06
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "attachments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class _Attachment implements Serializable {

    private static final long serialVersionUID = 4865835205392571227L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String attachmentId;
    @Field(name = "file_name")
    private String fileName;
    @Field(name = "full_path")
    private String fullPath;
    @Field(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;
}
