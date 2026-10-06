// Chain of Responsibility - Expense approval (AI-assisted version, reviewed and corrected)
// Added: CFO (200000), logging fallback, constructor injection, typed Decision.

public class Main {
    public static void main(String[] args) {
        Approver chain = new TeamLead(new Manager(new Director(new CFO(new LoggingFallback()))));
        System.out.println(chain.approve(new ExpenseRequest(4_200, "Team offsite")));
        System.out.println(chain.approve(new ExpenseRequest(150_000, "New servers")));
        System.out.println(chain.approve(new ExpenseRequest(900_000, "Office building")));
    }
}

enum Decision { APPROVED, DENIED }

class ExpenseRequest {
    final double amount; final String purpose;
    ExpenseRequest(double amount, String purpose) { this.amount = amount; this.purpose = purpose; }
}

abstract class Approver {
    private final String role;
    private final double limit;
    private final Approver next;
    Approver(String role, double limit, Approver next) {
        this.role = role; this.limit = limit; this.next = next;
    }
    public Decision approve(ExpenseRequest r) {                      // handle or forward
        if (r.amount <= limit) {
            System.out.println(role + " approved: " + r.purpose + " (" + r.amount + ")");
            return Decision.APPROVED;
        }
        return (next != null) ? next.approve(r) : Decision.DENIED;
    }
}
class TeamLead extends Approver { TeamLead(Approver n) { super("TeamLead", 1_000, n); } }
class Manager  extends Approver { Manager(Approver n)  { super("Manager", 5_000, n); } }
class Director extends Approver { Director(Approver n) { super("Director", 50_000, n); } }
class CFO      extends Approver { CFO(Approver n)      { super("CFO", 200_000, n); } }

class LoggingFallback extends Approver {                             // end of chain: nobody could approve
    LoggingFallback() { super("Fallback", 0, null); }
    @Override public Decision approve(ExpenseRequest r) {
        System.out.println("UNAPPROVED: " + r.purpose + " (" + r.amount + ") exceeds every limit");
        return Decision.DENIED;
    }
}
