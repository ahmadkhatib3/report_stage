// Bridge pattern - Notifications (AI-assisted version, reviewed and corrected)
// Added: WhatsappChannel, DigestNotification, non-null constructor check, typed SendResult.
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(new AlertNotification(new WhatsappChannel()).send("bob", "disk full"));
        System.out.println(new ReminderNotification(new SmsChannel()).send("alice", "meeting at 10"));
        System.out.println(new DigestNotification(new EmailChannel())
                .send("alice", List.of("build passed", "2 new comments", "deploy done")));
        try { new AlertNotification(null); }
        catch (NullPointerException e) { System.out.println("Rejected null channel: " + e.getMessage()); }
    }
}

record SendResult(boolean delivered, String detail) { }

interface Channel { SendResult send(String to, String body); }       // stays free of notification concerns

class EmailChannel implements Channel {
    public SendResult send(String to, String body) {
        System.out.println("[EMAIL -> " + to + "] " + body);
        return new SendResult(true, "email queued");
    }
}
class SmsChannel implements Channel {
    public SendResult send(String to, String body) {
        System.out.println("[SMS -> " + to + "] " + body);
        return new SendResult(true, "sms sent");
    }
}
class PushChannel implements Channel {
    public SendResult send(String to, String body) {
        System.out.println("[PUSH -> " + to + "] " + body);
        return new SendResult(true, "push delivered");
    }
}
class WhatsappChannel implements Channel {
    public SendResult send(String to, String body) {
        System.out.println("[WHATSAPP -> " + to + "] " + body);
        return new SendResult(true, "whatsapp delivered");
    }
}

abstract class Notification {
    private final Channel channel;                                   // injected, never null
    protected Notification(Channel c) { this.channel = Objects.requireNonNull(c, "channel must not be null"); }
    protected abstract String format(List<String> messages);
    public SendResult send(String to, List<String> messages) {
        return channel.send(to, format(messages));                   // still the bridge
    }
    public SendResult send(String to, String message) {              // keeps single-message callers working
        return send(to, List.of(message));
    }
}
class AlertNotification extends Notification {
    AlertNotification(Channel c) { super(c); }
    protected String format(List<String> m) { return "[ALERT] " + String.join(" | ", m); }
}
class ReminderNotification extends Notification {
    ReminderNotification(Channel c) { super(c); }
    protected String format(List<String> m) { return "Reminder: " + String.join(", ", m); }
}
class DigestNotification extends Notification {
    DigestNotification(Channel c) { super(c); }
    protected String format(List<String> m) { return m.size() + " updates: " + String.join("; ", m); }
}
