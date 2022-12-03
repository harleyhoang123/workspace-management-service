package vn.edu.fpt.workspace.dto.response.workspace;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.workspace.dto.common.AuditableResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 22:33
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class GetWorkspaceDetailResponse extends AuditableResponse {

    private static final long serialVersionUID = -7945346610861141221L;
    private String workspaceId;
}
