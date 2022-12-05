package vn.edu.fpt.workspace.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateWorkspaceEvent implements Serializable {

    private static final long serialVersionUID = -1004339438954694483L;
    private String projectId;
    private String accountId;

}
