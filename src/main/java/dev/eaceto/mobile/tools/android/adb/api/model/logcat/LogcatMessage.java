package dev.eaceto.mobile.tools.android.adb.api.model.logcat;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogcatMessage {

    private final String level;
    private final long timeStamp;
    private final Long pid;
    private final Long tid;
    private final String application;
    private final String tag;
    private final String text;

    public LogcatMessage(String level, long timeStamp, Long pid, Long tid, String application, String tag, String text) {
        this.level = level;
        this.timeStamp = timeStamp;
        this.pid = pid;
        this.tid = tid;
        this.application = application;
        this.tag = tag;
        this.text = text;
    }

    public static LogcatMessage waitingForDeviceMessage(Long pid) {
        return new LogcatMessage("I", System.currentTimeMillis(), pid, null, null, null, null);
    }

    public static LogcatMessage fromLogLine(String line, String application) {
        try {
            String[] parts = line.split(" ");

            ArrayList<String> args = new ArrayList<>();
            String text = "";
            int textStart = 0;
            for (String part : parts) {
                if (part.isBlank()) {
                    continue;
                }
                if (args.size() < 6) {
                    textStart += part.length();
                    if (part.endsWith(":")) {
                        textStart -= 1;
                        args.add(part.substring(0, part.length() - 1));
                    } else {
                        args.add(part);
                    }
                } else {
                    int separatorIndex = line.indexOf(":", textStart);
                    if (separatorIndex >= 0) {
                        text = line.substring(separatorIndex + 1);
                    }
                    break;
                }
            }
            if (args.size() < 6) return null;

            String level = LogcatMessage.logcatLevel(args.get(4));
            String tag = args.get(5).trim();
            Long pid, tid, timeStamp;

            if (level == null) return null;
            pid = Long.parseLong(args.get(2));
            tid = Long.parseLong(args.get(3));

            timeStamp = LogcatMessage.timeStrampFrom(args.get(0), args.get(1));
            if (timeStamp == null) return null;
            return new LogcatMessage(level, timeStamp, pid, tid, application, tag, text);
        } catch (Exception ignored) {
        }
        return null;
    }

    private static String logcatLevel(String level) {
        boolean validLevel = Arrays.asList(new String[]{"E", "W", "I", "D", "V"}).contains(level);
        if (!validLevel) return null;
        return level;
    }

    private static Long timeStrampFrom(String date, String time) {
        if (date == null || time == null) return null;
        date = date.trim();
        time = time.trim();
        if (date.isEmpty() || time.isEmpty()) return null;
        String dateTime = date + " " + time;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss.sss");
        try {
            return formatter.parse(dateTime).getTime();
        } catch (Exception ignored) {
            return null;
        }
    }

    public String getLevel() {
        return level;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Long getPID() {
        return pid;
    }

    public Long getTID() {
        return tid;
    }

    public String getApplication() {
        return application;
    }

    public String getTag() {
        return tag;
    }

    public String getText() {
        return text;
    }

    public String jsonString() {
        JSONObject object = new JSONObject();
        object.put("level", level);
        object.put("timeStamp", timeStamp);
        if (pid != null) {
            object.put("pid", pid);
        }
        if (tid != null) {
            object.put("tid", tid);
        }
        if (application != null) {
            object.put("application", application);
        }
        if (tag != null) {
            object.put("tag", tag);
        }
        if (text != null) {
            object.put("text", text);
        }
        return object.toString();
    }
}
