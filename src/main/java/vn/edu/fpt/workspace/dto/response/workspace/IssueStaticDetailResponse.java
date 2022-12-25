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
public class IssueStaticDetailResponse implements Serializable {

    private static final long serialVersionUID = -7145223354875542644L;
    private UserInfoResponse userInfo;
    private Long numOfTask;
}
