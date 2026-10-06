// Protection Proxy - Bank account (initial implementation)

public class Main {
    public static void main(String[] args) {
        RealBankAccount real = new RealBankAccount(1000);
        BankAccount asOwner  = new ProtectedBankAccount(real, new User("Owner", Role.OWNER));
        BankAccount asViewer = new ProtectedBankAccount(real, new User("Guest", Role.VIEWER));
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

interface BankAccount { void withdraw(double amount); double getBalance(); }   // Subject

class RealBankAccount implements BankAccount {                                  // RealSubject
    private double balance;
    RealBankAccount(double balance) { this.balance = balance; }
    public void withdraw(double amount) {
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
        System.out.println("Withdrew " + amount + ", new balance " + balance);
    }
    public double getBalance() { return balance; }
}

class ProtectedBankAccount implements BankAccount {                             // Proxy
    private final RealBankAccount account; private final User user;
    ProtectedBankAccount(RealBankAccount account, User user) { this.account = account; this.user = user; }
    public void withdraw(double amount) {
        if (user.hasRole(Role.OWNER)) account.withdraw(amount);
        else throw new SecurityException("OWNER role required");
    }
    public double getBalance() { return account.getBalance(); }                 // open to all
}
