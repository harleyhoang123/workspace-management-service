package vn.edu.fpt.workspace.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HandleNotifyEvent implements Serializable {

    private static final long serialVersionUID = 7142461852872990130L;
    private String accountId;
    private String content;
    private LocalDateTime createdDate;
}
