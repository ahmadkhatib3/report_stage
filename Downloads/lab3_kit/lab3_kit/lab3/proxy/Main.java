// Protection Proxy - Bank account (AI-assisted version, reviewed and corrected)
// Added: audit logging, injection of the BankAccount interface, injected AuditLog.

public class Main {
    public static void main(String[] args) {
        BankAccount real = new RealBankAccount(1000);
        AuditLog log = new ConsoleAuditLog();
        BankAccount asOwner  = new ProtectedBankAccount(real, new User("Owner", Role.OWNER), log);
        BankAccount asViewer = new ProtectedBankAccount(real, new User("Guest", Role.VIEWER), log);
        System.out.println("Balance (viewer): " + asViewer.getBalance());
        asOwner.withdraw(200);
        try { asViewer.withdraw(100); }
        catch (SecurityException e) { System.out.println("Denied: " + e.getMessage()); }
        System.out.println("Balance (owner): " + asOwner.getBalance());
    }
}

enum Role { OWNER, VIEWER }

class User {
    private final String name; private final Role role;
    User(String name, Role role) { this.name = name; this.role = role; }
    boolean hasRole(Role r) { return role == r; }
    String getName() { return name; }
}

interface BankAccount { void withdraw(double amount); double getBalance(); }

class RealBankAccount implements BankAccount {
    private double balance;
    RealBankAccount(double balance) { this.balance = balance; }
    public void withdraw(double amount) {
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
        System.out.println("Withdrew " + amount + ", new balance " + balance);
    }
    public double getBalance() { return balance; }
}

interface AuditLog { void record(User user, String action, double amount, boolean allowed); }

class ConsoleAuditLog implements AuditLog {
    public void record(User u, String action, double amount, boolean allowed) {
        System.out.println("AUDIT: " + u.getName() + " " + action + " " + amount
                + " -> " + (allowed ? "ALLOWED" : "DENIED"));
    }
}

class ProtectedBankAccount implements BankAccount {
    private final BankAccount target;                                // program to the interface
    private final User user;
    private final AuditLog audit;
    ProtectedBankAccount(BankAccount target, User user, AuditLog audit) {
        this.target = target; this.user = user; this.audit = audit;
    }
    public void withdraw(double amount) {
        boolean ok = user.hasRole(Role.OWNER);
        audit.record(user, "withdraw", amount, ok);                  // every attempt is logged, allowed or not
        if (!ok) throw new SecurityException("OWNER role required");
        target.withdraw(amount);
    }
    public double getBalance() { return target.getBalance(); }       // open to all
}
