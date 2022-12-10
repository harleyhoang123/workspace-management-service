package vn.edu.fpt.workspace.config.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.dto.event.GenerateProjectAppEvent;
import vn.edu.fpt.workspace.exception.BusinessException;
import vn.edu.fpt.workspace.service.WorkspaceService;

@Service
@RequiredArgsConstructor
public class GenerateProjectAppConsumer extends Consumer{

    private final WorkspaceService workspaceService;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(id = "generateProjectAppInWorkspaceConsumer", topics = "flab.project.generate-app", groupId = "workspace_group")
    protected void listen(String value, String topic, String key) {
        super.listen(value, topic, key);
        try {
            GenerateProjectAppEvent event = objectMapper.readValue(value, GenerateProjectAppEvent.class);
            workspaceService.createWorkspace(event);
        }catch (Exception ex){
            throw new BusinessException("Can't create workspace from event");
        }

    }
}
