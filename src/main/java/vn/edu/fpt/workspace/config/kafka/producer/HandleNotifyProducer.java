package vn.edu.fpt.workspace.config.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vn.edu.fpt.workspace.dto.event.HandleNotifyEvent;
import vn.edu.fpt.workspace.dto.event.SendEmailEvent;
import vn.edu.fpt.workspace.exception.BusinessException;

import java.util.UUID;

@Service
@Slf4j
public class HandleNotifyProducer extends Producer{
    private static final String TOPIC = "flab.notification.handle";
    private ObjectMapper objectMapper;

    @Autowired
    public HandleNotifyProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        super(kafkaTemplate);
        this.objectMapper = objectMapper;
    }

    public void sendMessage(HandleNotifyEvent event) {
        try {
            String value = objectMapper.writeValueAsString(event);
            super.sendMessage("flab.workspace.add-member-to-workspace", UUID.randomUUID().toString(), value);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Can't convert Add Member To Workspace event to String: " + e.getMessage());
        }
    }
}
