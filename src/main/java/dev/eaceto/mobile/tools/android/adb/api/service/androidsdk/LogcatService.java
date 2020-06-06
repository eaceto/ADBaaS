package dev.eaceto.mobile.tools.android.adb.api.service.androidsdk;

import dev.eaceto.mobile.tools.android.adb.api.exceptions.ApplicationNotFoundException;
import dev.eaceto.mobile.tools.android.adb.api.exceptions.ApplicationNotRunningException;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.Application;
import dev.eaceto.mobile.tools.android.adb.api.model.logcat.LogcatMessage;
import dev.eaceto.mobile.tools.android.adb.api.model.logcat.LogcatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class LogcatService extends ADBService {

    private final String EVENTNAME_START = "start";
    private final String EVENTNAME_END = "end";
    private final String EVENTNAME_LOG = "log";
    private final String EVENTNAME_WAITING_FOR_DEVICE = "waiting_device";
    private final HashMap<String, String> activeSessions = new HashMap<>(); //key: Device + PackageName / Value: SessionId
    private final HashMap<String, ArrayList<SseEmitter>> registeredEmitters = new HashMap<>(); //key: SessionId / Value: List of subscribers
    private final HashMap<String, ExecutorService> activeExecutors = new HashMap<>(); //key: SessionId / Value: ExecutorService

    @Autowired
    ApplicationService applicationService;

    public SseEmitter createEmitter(String deviceId, String packageName) throws Exception {
        Application application = applicationService.getApplication(deviceId, packageName, false);
        if (application == null) {
            throw new ApplicationNotFoundException(deviceId, packageName);
        }

        Long pid = applicationService.getApplicationPID(deviceId, packageName);
        if (pid == null || pid <= 0) {
            throw new ApplicationNotRunningException(deviceId, packageName);
        }

        LogcatSession logcatSession = createLogcatSession(deviceId, packageName, pid);

        SseEmitter emitter = new SseEmitter();
        registerEmitter(logcatSession, emitter);

        String sessionId = logcatSession.getSessionId();
        ExecutorService executor = activeExecutors.getOrDefault(sessionId, Executors.newSingleThreadExecutor());

        if (!activeExecutors.containsKey(sessionId)) {
            activeExecutors.put(sessionId, executor);

            executor.execute(() -> {
                try {
                    Process p = executeProcess(adb, "-s " + deviceId + " logcat --pid " + pid);

                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        LogcatMessage message = null;
                        String eventName = null;
                        if ("- waiting for device -".equalsIgnoreCase(line)) {
                            message = LogcatMessage.waitingForDeviceMessage(pid);
                            eventName = EVENTNAME_WAITING_FOR_DEVICE;
                        } else {
                            message = LogcatMessage.fromLogLine(line, packageName);
                            eventName = EVENTNAME_LOG;
                        }

                        if (message != null) {
                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .data(message.jsonString())
                                    .name(eventName);
                            emitter.send(event);
                        }
                    }
                    br.close();

                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(logcatSession.finished().jsonString())
                            .name(EVENTNAME_END);

                    emitter.send(event);
                } catch (Exception ex) {
                    close(logcatSession);
                }
            });
        }

        emitter.onCompletion(() -> close(logcatSession, emitter));
        emitter.onTimeout(() -> close(logcatSession, emitter));

        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .data(logcatSession.started().jsonString())
                .name(EVENTNAME_START);
        emitter.send(event);

        return emitter;
    }

    private void close(LogcatSession logcatSession, SseEmitter emitter) {
        List<SseEmitter> emitters = registeredEmitters.getOrDefault(logcatSession.getSessionId(), null);
        if (emitters != null) {
            ArrayList<SseEmitter> list = new ArrayList<>(emitters.parallelStream().filter(anEmitter -> anEmitter != emitter).collect(Collectors.toList()));
            if (list.isEmpty()) {
                close(logcatSession);
            } else {
                registeredEmitters.put(logcatSession.getSessionId(), list);
            }
        }
    }

    private void close(LogcatSession logcatSession) {
        String sessionId = logcatSession.getSessionId();
        List<SseEmitter> emitters = registeredEmitters.getOrDefault(sessionId, null);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.complete();
                } catch (Exception ignored) {
                }
            }
        }
        registeredEmitters.remove(sessionId);
        try {
            ExecutorService executor = activeExecutors.getOrDefault(sessionId, null);
            executor.shutdown();
        } catch (Exception ignored) {
        }
        activeExecutors.remove(sessionId);
        activeSessions.remove(generateSessionKey(logcatSession.getDeviceId(), logcatSession.getPackageName()));
    }

    private LogcatSession createLogcatSession(@NonNull String deviceId, String packageName, Long pid) {
        String key = generateSessionKey(deviceId, packageName);
        String sessionId = activeSessions.getOrDefault(key, null);
        if (sessionId != null) {
            return new LogcatSession(deviceId, packageName, sessionId);
        }
        sessionId = UUID.randomUUID().toString();
        activeSessions.put(key, sessionId);
        LogcatSession session = new LogcatSession(deviceId, packageName, sessionId);
        session.setPID(pid);
        return session;
    }

    private String generateSessionKey(@NonNull String deviceId, String packageName) {
        return (packageName != null ? packageName : "*") + deviceId;
    }

    private void registerEmitter(LogcatSession logcatSession, SseEmitter emitter) {
        ArrayList<SseEmitter> emitters = registeredEmitters.getOrDefault(logcatSession.getSessionId(), new ArrayList<>());
        emitters.add(emitter);
        registeredEmitters.put(logcatSession.getSessionId(), emitters);
    }

}
