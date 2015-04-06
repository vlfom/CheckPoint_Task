package operator;

import display.Table;
import user.User;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Session {
    public static Table table ;
    User userIn, userOut ;
    private long exectedLength ;
    private long startTime ;
    ScheduledFuture<?> future ;

    public Session( User from, User to, long expectedLength ) {
        this.userIn = from ;
        this.userOut = to ;
        this.exectedLength = expectedLength ;

        this.startTime = System.currentTimeMillis() ;

        this.future = Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
                new myRunnable(this), expectedLength, expectedLength, TimeUnit.MILLISECONDS
        );
    }

    public User getAnother(User user) {
        if( userIn.equals(user) )
            return userOut ;
        return userIn ;
    }

    public boolean isFinished() {
        long currentTime = System.currentTimeMillis() ;
        return currentTime > startTime + exectedLength ;
    }

    public void finish() {
        this.userIn.notifyFree();
        this.userOut.notifyFree();
    }

    public long getLength() {
        return System.currentTimeMillis() - startTime ;
    }
}

class myRunnable implements Runnable {
    Session session ;

    public myRunnable(Session session) {
        this.session = session ;
    }

    @Override
    public void run() {
//        System.out.printf("#CALL__END:  %8s   call to                %8s   has just finished after %3.1f seconds.\n",
//                session.userIn.name, session.userOut.name, session.getLength()/1000.0) ;
        session.userIn.notifyCallEnded() ;
        session.userOut.notifyCallEnded() ;
        session.future.cancel(true) ;
    }
}