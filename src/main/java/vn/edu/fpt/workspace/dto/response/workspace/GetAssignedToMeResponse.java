package vn.edu.fpt.workspace.dto.response.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;
import vn.edu.fpt.workspace.service.UserInfoService;

import java.io.Serializable;

@AllArgsConstructor
@Data
@Builder
public class GetAssignedToMeResponse implements Serializable {

    private static final long serialVersionUID = -3885087610771634562L;
    private String issueName;
    private Integer estimate;
    private UserInfoResponse reporter;
}
