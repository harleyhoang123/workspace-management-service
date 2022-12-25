package vn.edu.fpt.workspace.dto.response.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.workspace.dto.common.ActivityResponse;
import vn.edu.fpt.workspace.dto.common.PageableRequest;
import vn.edu.fpt.workspace.dto.common.PageableResponse;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetActivityStreamResponse implements Serializable {

    private static final long serialVersionUID = 3462504741379925653L;
    List<ActivityResponse> activities;
}
