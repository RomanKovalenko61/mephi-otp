package ru.mephi.mephiotp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Slf4j
@Service
public class TelegramNotificationService {

    private final String botToken;
    private final String chatId;
    private final RestTemplate restTemplate;

    public TelegramNotificationService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.chat.id}") String chatId) {
        this.botToken = botToken;
        this.chatId = chatId;
        this.restTemplate = new RestTemplate();
    }

    public void sendMessage(String message) {
        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                botToken, chatId, message.replace(" ", "%20")
        );
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("Telegram message sent: {}", response.getBody());
        } catch (Exception e) {
            log.error("Error sending Telegram message", e);
        }
    }
}
