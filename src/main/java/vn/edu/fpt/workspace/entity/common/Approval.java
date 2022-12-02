package vn.edu.fpt.workspace.entity.common;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 30/08/2022 - 19:36
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
@SuperBuilder
public abstract class Approval extends Auditor implements Serializable {

    private static final long serialVersionUID = -1117386677524051821L;
    @Field(name = "status")
    private String status;
    @Field(name = "approved_by")
    private String approvedBy;
    @Field(name = "approved_date")
    private LocalDateTime approvedDate;
}
