package vn.edu.fpt.workspace.dto.response.story;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.UserInfoResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 30/11/2022 - 19:58
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateStoryResponse implements Serializable {
    private static final long serialVersionUID = 6647338810156614575L;
    private String storyId;
    private String storyName;
}
