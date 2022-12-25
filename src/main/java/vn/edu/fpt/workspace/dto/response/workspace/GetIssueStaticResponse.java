package vn.edu.fpt.workspace.dto.response.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetIssueStaticResponse implements Serializable {

    private static final long serialVersionUID = 8015927946398799541L;
    private long totalIssue;
    private long totalUnassigned;
    private List<IssueStaticDetailResponse> issueStatic;

}
