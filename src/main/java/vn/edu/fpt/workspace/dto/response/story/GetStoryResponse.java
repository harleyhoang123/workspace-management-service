package vn.edu.fpt.workspace.dto.response.story;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:07
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetStoryResponse implements Serializable {

    private static final long serialVersionUID = 3071771222131304649L;
    private String key;
    private String storyName;
    private String estimateTime;
    private String assignee;
    private String status;
}
