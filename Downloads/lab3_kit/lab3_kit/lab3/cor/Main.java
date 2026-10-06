// Chain of Responsibility - Expense approval (initial implementation)

public class Main {
    public static void main(String[] args) {
        Approver lead = new TeamLead(), mgr = new Manager(), dir = new Director();
        lead.setNext(mgr); mgr.setNext(dir);                         // wiring is the caller's job
        ExpenseRequest req = new ExpenseRequest();
        req.amount = 4200; req.purpose = "Team offsite";
        lead.approve(req);                                           // prints: Manager: Team offsite
    }
}

class ExpenseRequest { double amount; String purpose; }

abstract class Approver {
    protected Approver next;
    protected double limit;
    protected String role;
    public void setNext(Approver n) { this.next = n; }
    public void approve(ExpenseRequest r) {                          // handle or forward
        if (r.amount <= limit) System.out.println(role + ": " + r.purpose);
        else if (next != null) next.approve(r);
    }
}
class TeamLead extends Approver { TeamLead() { limit = 1000;  role = "TeamLead"; } }
class Manager  extends Approver { Manager()  { limit = 5000;  role = "Manager"; } }
class Director extends Approver { Director() { limit = 50000; role = "Director"; } }
