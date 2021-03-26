import java.util.*;

public class InBetweenCPU {

    private final double[] increments = {3.0, 2.5, 2.25, 1.75, 1.3, 1.0, 0.75, 0.6, 0.5};
    public int aggression;
    public double balance;

    public InBetweenCPU(int aggression, double balance) {
        this.aggression = aggression;
        this.balance = balance;
    }

    public double decideBet(double first_val, double second_val, double pot) {
        double limit = pot;
        if (pot > this.balance / 2.0) {
            limit = this.balance / 2.0;
            if (this.balance <= 1.00) {
                limit = this.balance;
            }
        }
        double diff = Math.abs(first_val - second_val);
        if (diff < 3) {
            return 0.0;
        }
        double win_prob = (diff - 1) / 13;
        double percentage = Math.pow(win_prob, this.increments[this.aggression - 1]);
        if (percentage < 0.1) {
            return 0.0;
        }
        else if (percentage > 0.8) {
            return limit;
        }
        return Math.round((Math.round(percentage * limit * 100.0) / 100.0) * 4f) / 4f;
    }

    public boolean decideSplit(int value) {
        int range = (int) Math.ceil((this.aggression - 1) / 2.0);
        return value <= 2 + range || value >= 14 - range;
    }

}
