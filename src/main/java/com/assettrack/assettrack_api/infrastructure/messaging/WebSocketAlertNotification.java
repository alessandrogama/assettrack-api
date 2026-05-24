package com.assettrack.assettrack_api.infrastructure.messaging;

import com.assettrack.assettrack_api.application.dto.response.AlertResponse;
import com.assettrack.assettrack_api.application.usecase.alert.AlertNotificationPort;
import com.assettrack.assettrack_api.domain.entity.Alert;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


@Component
public class WebSocketAlertNotification implements AlertNotificationPort {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketAlertNotification(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notify(Alert alert) {
        AlertResponse payload = AlertResponse.from(alert);

        messagingTemplate.convertAndSend("/topic/alerts", payload);

        messagingTemplate.convertAndSend(
                "/topic/alerts/" + alert.getAssetId(),
                payload
        );
    }
}
