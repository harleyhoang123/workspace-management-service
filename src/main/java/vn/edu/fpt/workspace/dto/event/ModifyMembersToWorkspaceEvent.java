package vn.edu.fpt.workspace.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.entity.MemberInfo;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ModifyMembersToWorkspaceEvent implements Serializable {
    private static final long serialVersionUID = 5616410352274104603L;
    private String workspaceId;
    private String accountId;
    private String type;
}
