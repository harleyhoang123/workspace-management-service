package vn.edu.fpt.workspace.dto.request.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.workspace.dto.common.AuditableRequest;

@AllArgsConstructor
@Data
@SuperBuilder
public class GetActivityStreamRequest extends AuditableRequest {

    private static final long serialVersionUID = -4505129799340509296L;
}
