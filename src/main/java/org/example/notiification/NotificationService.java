package org.example.notiification;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;

    public NotificationService(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter subscribe(Long userId, String lastEventId) {
        String sseId = String.valueOf(userId);
//        String sseId = userId + "_" + System.currentTimeMillis();

        SseEmitter sseEmitter = emitterRepository.save(sseId, new SseEmitter(DEFAULT_TIMEOUT));
        sseEmitter.onCompletion(() -> {
            emitterRepository.deleteById(sseId);
        });
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(sseId));

        sendNotification(sseEmitter, sseId, "data");

        return sseEmitter;
    }

    public void publish(Long userId) {
        String sseId = String.valueOf(userId);
        SseEmitter sseEmitter = emitterRepository.findById(sseId);

        sendNotification(sseEmitter, sseId, "publish");
    }

    private void sendNotification(SseEmitter emitter, String id, Object data) {

        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {

            throw new RuntimeException(e);
        }


    }
}
