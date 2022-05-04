package org.example.notiification;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    private final Map<String, SseEmitter> sseEmitterMap;

    public EmitterRepository() {
        sseEmitterMap = new ConcurrentHashMap<>();
    }

    public SseEmitter save(String sseId, SseEmitter sseEmitter) {
        sseEmitterMap.put(sseId, sseEmitter);
        return sseEmitter;
    }

    public SseEmitter deleteById(String sseId) {
        return sseEmitterMap.remove(sseId);
    }

    public SseEmitter findById(String sseId) {
        return sseEmitterMap.get(sseId);
    }
}
