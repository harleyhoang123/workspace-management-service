package vn.edu.fpt.workspace.dto.response.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetMemberInWorkspaceResponse implements Serializable {
    private static final long serialVersionUID = 4708357358314995794L;
    private String memberId;
    private UserInfoResponse userInfo;

}
