package vn.edu.fpt.workspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:05
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -1769941486423477201L;
    private String userId;
    private String role;
    private String fullName;
    private String email;
}
