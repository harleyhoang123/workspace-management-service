package vn.edu.fpt.workspace.config.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.dto.event.ModifyMembersToWorkspaceEvent;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.service.WorkspaceService;

@Service
@RequiredArgsConstructor
public class ModifyMembersToWorkspaceConsumer extends Consumer{
    private final WorkspaceService workspaceService;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(id = "modifyMembersToWorkspaceConsumer", topics = "flab.workspace.add-member-to-workspace", groupId = "workspace_group")
    protected void listen(String value, String topic, String key) {
        super.listen(value, topic, key);
        try {
            ModifyMembersToWorkspaceEvent event = objectMapper.readValue(value, ModifyMembersToWorkspaceEvent.class);
            workspaceService.modifyMembersToWorkspace(event);
        }catch (Exception ex){
            throw new BusinessException("Can't modify members to workspace from event");
        }

    }
}
