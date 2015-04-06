package user;

import display.Table;
import operator.Operator;
import operator.Session;

import java.time.LocalDateTime;

public class User {
    public static Table table ;
    public int ID ;
    public String name;
    public Operator operator;
    public double money;
    public LocalDateTime lastSms ;
    public double lastUpAmount ;
    public LocalDateTime lastUp ;
    public boolean upMarked ;

    private Session session;

    public User(int ID, String name, double money, Operator operator) {
        this.ID = ID ;
        this.name = name;
        this.money = money;
        this.operator = operator;
        this.lastSms = null ;
        this.lastUp = null ;
        this.upMarked = false ;
    }

    synchronized public boolean changeOperator(Operator operator, boolean premium) {
        if (this.operator == operator)
            return true;
        if (operator.getStandardSubscriptionCost() > this.money)
            return false;
        this.money -= operator.getStandardSubscriptionCost();
        this.operator = operator;
        return true;
    }

    synchronized public void checkSession() {
        if (session != null && session.isFinished()) {
            session.finish();
            session = null;
        }
    }

    synchronized public User getCompanion() {
        checkSession();
        if( session == null )
            return null ;
        return session.getAnother(this) ;
    }

    synchronized public void notifyCallEnded() {
        session = null ;
        this.busy = false ;
    }

    private boolean busy;

    synchronized public boolean isBusy() {
        checkSession();
        return this.busy;
    }

    synchronized public boolean call(User u, long expectedLength) {
        if (u == this)
            return false;
        if (u.isBusy()) {
            //System.out.printf("#CALL_BUSY:  %8s   tried to call          %8s   but they were busy.\n", this.name, u.name);
            return false;
        }
        if(this.money < this.operator.getCallCost(u.operator.ID)) {
//            System.out.printf("#CALL_CASH:  %8s   tried to call          %8s   but it costs %.2f while he had %.2f only.\n",
//                    this.name, u.name,
//                    this.operator.getCallCost(u.operator.ID),
//                    this.money);
            return false ;
        }
        this.money -= this.operator.getCallCost(u.operator.ID);
        this.busy = u.busy = true;
        this.session = u.session = new Session(this, u, expectedLength);
        return true;
    }

    synchronized public boolean sms(User u) {
        if (u == this)
            return false;
        if(this.money < this.operator.getSmsCost(u.operator.ID)) {
//            System.out.printf("#SMS__CASH:  %8s   tried to send sms to   %8s   but it costs %.2f while he had %.2f only.\n",
//                    this.name, u.name,
//                    this.operator.getSmsCost(u.operator.ID),
//                    this.money);
            return false ;
        }
        this.lastSms = LocalDateTime.now() ;
        this.money -= this.operator.getSmsCost(u.operator.ID);
        return true;
    }

    public void notifyFree() {
        this.busy = false;
    }
}