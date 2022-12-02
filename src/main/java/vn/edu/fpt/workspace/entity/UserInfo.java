package vn.edu.fpt.workspace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:28
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -5846047449784455813L;
    @Field(name = "account_id")
    private String accountId;
    @Field(name = "full_name")
    private String fullName;
    @Field(name = "email")
    private String email;
    @Field(name = "role")
    private String role;
}
