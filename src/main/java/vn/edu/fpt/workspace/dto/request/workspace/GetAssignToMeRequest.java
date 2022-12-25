package vn.edu.fpt.workspace.dto.request.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetAssignToMeRequest implements Serializable {

    private static final long serialVersionUID = 8690185679750055965L;
    private String memberId;
}
