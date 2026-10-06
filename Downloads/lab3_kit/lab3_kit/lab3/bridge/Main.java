// Bridge pattern - Notifications (initial implementation)
// Abstraction: Notification (Alert, Reminder)  |  Implementor: Channel (Email, SMS, Push)

public class Main {
    public static void main(String[] args) {
        new AlertNotification(new EmailChannel()).send("bob", "disk full");
        new ReminderNotification(new SmsChannel()).send("alice", "meeting at 10");
        // Third channel, zero change to the notification hierarchy:
        new AlertNotification(new PushChannel()).send("bob", "disk full");
    }
}

interface Channel { void send(String to, String body); }            // Implementor

class EmailChannel implements Channel {
    public void send(String to, String body) { System.out.println("[EMAIL -> " + to + "] " + body); }
}
class SmsChannel implements Channel {
    public void send(String to, String body) { System.out.println("[SMS -> " + to + "] " + body); }
}
class PushChannel implements Channel {                               // added later, no Notification change
    public void send(String to, String body) { System.out.println("[PUSH -> " + to + "] " + body); }
}

abstract class Notification {                                        // Abstraction
    protected final Channel channel;                                 // the bridge
    protected Notification(Channel c) { this.channel = c; }
    protected abstract String format(String text);
    public void send(String to, String text) { channel.send(to, format(text)); }
}
class AlertNotification extends Notification {                      // Refined abstraction
    AlertNotification(Channel c) { super(c); }
    protected String format(String t) { return "[ALERT] " + t; }
}
class ReminderNotification extends Notification {                   // Refined abstraction
    ReminderNotification(Channel c) { super(c); }
    protected String format(String t) { return "Reminder: " + t; }
}
