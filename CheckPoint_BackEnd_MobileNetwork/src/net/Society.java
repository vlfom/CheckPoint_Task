package net;

import operator.Operator;
import operator.Session;
import user.User;
import display.Table ;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Society {
    static ArrayList<User> users;
    static ArrayList<Operator> operators;
    static ScheduledExecutorService usersThread;
    static Table table;

    public static void main(String args[]) {
        table = new Table();
        User.table = table;
        Session.table = table;

        operators = new ArrayList<Operator>();

        operators.add(
                (new Operator("MTS", 0))
                        .setStandardSubscriptionCost(1)
                        .setPremiumSubscriptionCost(5)
        );
        operators.add(
                (new Operator("Kievstar", 1))
                        .setStandardSubscriptionCost(1)
                        .setPremiumSubscriptionCost(5)
        );
        operators.add(
                (new Operator("Life", 2))
                        .setStandardSubscriptionCost(1)
                        .setPremiumSubscriptionCost(5)
        );
        operators.add(
                (new Operator("Beeline", 3))
                        .setStandardSubscriptionCost(1)
                        .setPremiumSubscriptionCost(5)
        );
        operators.add(
                (new Operator("UMS", 4))
                        .setStandardSubscriptionCost(1)
                        .setPremiumSubscriptionCost(5)
        );
        operators.add(
                (new Operator("DJuice", 5))
                        .setStandardSubscriptionCost(1)
                        .setPremiumSubscriptionCost(5)
        );

        Random random = new Random();
        for (int j = 0; j < operators.size(); ++j) {
            Operator operator = operators.get(j);
            for (int i = 0; i < operators.size(); ++i) {
                if (operators.get(i).equals(operator))
                    operator
                            .setCallCost(i, 0.1 + random.nextDouble() * 0.4)
                            .setSmsCost(i, 0.05 + random.nextDouble() * 0.2);
                else
                    operator
                            .setCallCost(i, 0.5 + random.nextDouble() * 0.5)
                            .setSmsCost(i, 0.1 + random.nextDouble() * 0.4);
            }
            operators.set(j, operator);
        }

        users = new ArrayList<User>();

        NameGenerator nameGenerator = new NameGenerator();
        for (int i = 0; i < 50; ++i)
            users.add(new User(
                    i,
                    nameGenerator.getName(),
                    1 + random.nextDouble() * 4,
                    operators.get(random.nextInt(operators.size()))
            ));

        usersThread = Executors.newSingleThreadScheduledExecutor();
        for (int i = 0; i < users.size(); ++i)
            usersThread.scheduleWithFixedDelay(new myRunnable(users.get(i)), 1000 + 100 * (i % 5), 1000, TimeUnit.MILLISECONDS);

        usersThread.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Table.updateAll();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        Table.users = users;
    }
}

class myRunnable implements Runnable {
    private User user;
    private Random random;

    public myRunnable(User user) {
        this.user = user;
        random = new Random();
    }

    @Override
    public void run() {
        user.checkSession();
        if (!user.isBusy() && random.nextBoolean()) {
            if (random.nextInt(4) < 3)
                user.call(Society.users.get(random.nextInt(Society.users.size())), (1 + random.nextInt(3)) * 1000);
            else
                user.sms(Society.users.get(random.nextInt(Society.users.size())));
        }
        if(user.money < 0.5 && random.nextInt(4) == 3) {
            user.lastUpAmount = 1 + random.nextDouble() * 4 ;
            user.money += user.lastUpAmount ;
            user.upMarked = true ;
            user.lastUp = LocalDateTime.now() ;
            Table.markCell(user.ID, 1);
        }
        if(user.upMarked && Duration.between(user.lastUp, LocalDateTime.now()).getSeconds() > 3) {
            user.upMarked = false ;
            Table.unmarkCell(user.ID, 1);
        }
    }
}

class NameGenerator {
    private List vocals = new ArrayList();
    private List startConsonants = new ArrayList();
    private List endConsonants = new ArrayList();
    private List nameInstructions = new ArrayList();

    public NameGenerator() {
        String demoVocals[] = { "a", "e", "i", "o", "u", "ei", "ai", "ou", "j",
                "ji", "y", "oi", "au", "oo" };

        String demoStartConsonants[] = { "b", "c", "d", "f", "g", "h", "k",
                "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z",
                "ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh",
                "th" };

        String demoEndConsonants[] = { "b", "d", "f", "g", "h", "k", "l", "m",
                "n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
                "sh", "th", "tt", "ss", "pf", "nt" };

        String nameInstructions[] = { "vd", "cvdvd", "cvd", "vdvd" };

        this.vocals.addAll(Arrays.asList(demoVocals));
        this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
        this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
        this.nameInstructions.addAll(Arrays.asList(nameInstructions));
    }

    public NameGenerator(String[] vocals, String[] startConsonants,
                         String[] endConsonants) {
        this.vocals.addAll(Arrays.asList(vocals));
        this.startConsonants.addAll(Arrays.asList(startConsonants));
        this.endConsonants.addAll(Arrays.asList(endConsonants));
    }

    public NameGenerator(String[] vocals, String[] startConsonants,
                         String[] endConsonants, String[] nameInstructions) {
        this(vocals, startConsonants, endConsonants);
        this.nameInstructions.addAll(Arrays.asList(nameInstructions));
    }

    public String getName() {
        return firstCharUppercase(getNameByInstructions(getRandomElementFrom(nameInstructions)));
    }

    private int randomInt(int min, int max) {
        return (int) (min + (Math.random() * (max + 1 - min)));
    }

    private String getNameByInstructions(String nameInstructions) {
        String name = "";
        int l = nameInstructions.length();

        for (int i = 0; i < l; i++) { char x = nameInstructions.charAt(0); switch (x) { case 'v': name += getRandomElementFrom(vocals); break; case 'c': name += getRandomElementFrom(startConsonants); break; case 'd': name += getRandomElementFrom(endConsonants); break; } nameInstructions = nameInstructions.substring(1); } return name; } private String firstCharUppercase(String name) { return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1); } private String getRandomElementFrom(List v) {
        return (String) v.get(randomInt(0, v.size() - 1));
    }
}