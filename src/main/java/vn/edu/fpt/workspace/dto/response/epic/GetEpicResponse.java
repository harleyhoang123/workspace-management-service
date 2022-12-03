package vn.edu.fpt.workspace.dto.response.epic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.response.story.GetStoryResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 14:54
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetEpicResponse implements Serializable {

    private static final long serialVersionUID = 8618577821236342885L;
    private String epicId;
    private String key;
    private String epicName;
    private String status;
    private String description;
    private String assignee;
    private String label;
    private String startDate;
    private String dueDate;
    private String reporter;
    private List<GetStoryResponse> childIssue;
    private List<ActivityResponse> activity;
}
