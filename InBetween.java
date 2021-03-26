import java.util.*;

public class InBetween {

    private final Scanner sc = new Scanner(System.in);
    public Random RANDOM = new Random();
    public int num_players;
    public int turn;
    public Deck deck;
    public double user_balance;
    public double pot;
    public InBetweenCPU[] players;
    public boolean first_turn_switch = true;

    public InBetween(int num_players, double buy_in, double player_balance) {
        System.out.println("On a scale of 1 to 9, how aggressive do you want the CPUs to be?");
        int aggression = Integer.parseInt(sc.nextLine());
        this.num_players = num_players;
        this.pot = buy_in * this.num_players;
        this.user_balance = player_balance;
        this.deck = new Deck();
        this.deck.shuffle();
        this.players = new InBetweenCPU[this.num_players];
        for (int i = 1; i < this.num_players; i++) {
            this.players[i] = new InBetweenCPU(aggression, player_balance);
        }
        this.turn = RANDOM.nextInt(num_players);
    }

    public void cpuTurn(int player) {
        if (this.deck.size() < 3) {
            this.deck.shuffle();
        }
        System.out.println("It is now Player "+player+"'s turn.");
        if (this.players[player].balance == 0.0) {
            System.out.println("Player "+player+" is out of money.");
            return;
        }
        Card first = this.deck.draw();
        this.cpuTurn(player, first);
    }

    public void cpuTurn(int player, Card first) {
        if (first.num.equals("A")) {
            System.out.println(first.toString());
            if (RANDOM.nextBoolean()) {
                first.aceHigh = true;
                System.out.println("Player "+player+" chose ace high.");
            }
            else {
                System.out.println("Player "+player+" chose ace low.");
            }
        }
        Card second = this.deck.draw();
        second.aceHigh = true;
        System.out.println(first.toString() + " ;    ; " + second.toString());
        if (Math.abs(first.getValue() - second.getValue()) == 1.0) {
            if (first.suit.equals(second.suit)) {
                System.out.println("Cards one apart and same suit, so Player "+player+" loses $0.50.");
                this.players[player].balance -= 0.50;
                this.pot += 0.50;
            }
            else {
                System.out.println("Cards one apart, so Player "+player+" loses $0.25.");
                this.players[player].balance -= 0.25;
                this.pot += 0.25;
            }
            System.out.println("The pot is now $"+this.pot+".");
            return;
        }
        if (first.getValue() == second.getValue()) {
            this.makeCpuSplit(player, first, second);
            return;
        }
        double bet = this.players[player].decideBet(first.getValue(), second.getValue(), this.pot);
        if (bet > 1.00 && this.first_turn_switch) {
            bet = 1.00;
        }
        if (bet == 0.0) {
            System.out.println("Player "+player+" does not want to bet.");
            return;
        }
        else {
            System.out.println("Player "+player+" bets $"+bet+".");
        }
        Card third = this.deck.draw();
        third.aceHigh = true;
        System.out.println(first.toString()+" ; "+third.toString()+" ; "+second.toString());
        if (third.sameNumber(first) || third.sameNumber(second)) {
            if (this.players[player].balance - (bet * 2) < 0) {
                System.out.println("Player "+player+" has lost the rest of their money.");
                this.pot += this.players[player].balance;
                this.players[player].balance = 0.0;
                System.out.println("The pot is now $"+this.pot+".");
                return;
            }
            System.out.println("Same card, so Player "+player+" pays double his bet.");
            this.players[player].balance -= bet * 2;
            this.pot += bet * 2;
            System.out.println("The pot is now $"+this.pot+".");
            return;
        }
        if (third.getValue() > Math.min(first.getValue(), second.getValue()) &&
            third.getValue() < Math.max(first.getValue(), second.getValue())) {
            System.out.println("In Between! Player "+player+" has won $"+bet+".");
            this.players[player].balance += bet;
            this.pot -= bet;
        }
        else {
            System.out.println("Not in between, so Player "+player+" loses $"+bet+".");
            this.players[player].balance -= bet;
            this.pot += bet;
        }
        System.out.println("The pot is now $"+this.pot+".");
    }

    public void makeCpuSplit(int player, Card card1, Card card2) {
        if (this.deck.size() < 3) {
            this.deck.shuffle();
        }
        int value = card1.getValue();
        if (!this.players[player].decideSplit(value)) {
            System.out.println("Player "+player+" does not want to split.");
            return;
        }
        System.out.println("Player "+player+" wants to split.");
        System.out.println("The first split:");
        this.cpuTurn(player, card1);
        System.out.println("The second split:");
        this.cpuTurn(player, card2);
    }

    public void userTurn() {
        if (this.deck.size() < 3) {
            this.deck.shuffle();
        }
        System.out.println("It is now your turn.");
        System.out.println("CURRENT POT: " + this.pot);
        Card first = this.deck.draw();
        this.userTurn(first);
    }

    public void userTurn(Card first) {
        if (this.deck.size() < 3) {
            this.deck.shuffle();
        }
        if (first.num.equals("A")) {
            System.out.println(first.toString());
            System.out.println("Ace high or low?");
            String response = sc.nextLine();
            while (!(response.equals("high") || response.equals("low"))) {
                System.out.println("Please enter 'high' or 'low'.");
                response = sc.nextLine();
            }
            if (response.equals("high")) {
                first.aceHigh = true;
            }
        }
        Card second = this.deck.draw();
        second.aceHigh = true;
        System.out.println(first.toString() + " ;    ; " + second.toString());
        if (Math.abs(first.getValue() - second.getValue()) == 1.0) {
            if (first.suit.equals(second.suit)) {
                System.out.println("Cards one apart and same suit, so you lose $0.50.");
                this.user_balance -= 0.50;
                this.pot += 0.50;
            }
            else {
                System.out.println("Cards one apart, so you lose $0.25.");
                this.user_balance -= 0.25;
                this.pot += 0.25;
            }
            System.out.println("Your balance is now $"+user_balance+".");
            System.out.println("The pot is now $"+this.pot+".");
            return;
        }
        if (first.getValue() == second.getValue()) {
            System.out.println("Would you like to split?");
            String response = sc.nextLine();
            while (!(response.equals("yes") || response.equals("no"))) {
                System.out.println("Please enter 'yes' or 'no'.");
                response = sc.nextLine();
            }
            if (response.equals("yes")) {
                System.out.println("The first split:");
                this.userTurn(first);
                System.out.println("The second split:");
                this.userTurn(second);
            }
            return;
        }
        System.out.println("Enter your bet. (Enter 0 to pass)");
        double bet = Double.parseDouble(sc.nextLine());
        while (bet > (this.user_balance / 2)) {
            if (this.user_balance > 1.00) {
                System.out.println("You can bet at most half your balance. Try again.");
                bet = Double.parseDouble(sc.nextLine());
            }
        }
        while (bet > this.pot) {
            System.out.println("You can not bet more than the pot. Try again.");
            bet = Double.parseDouble(sc.nextLine());
        }
        while (bet > 1.00 && this.first_turn_switch) {
            System.out.println("You can bet at most 1.00 on the first turn. Try again.");
            bet = Double.parseDouble(sc.nextLine());
        }
        if (bet == 0.0) {
            System.out.println("You passed.");
            return;
        }
        Card third = this.deck.draw();
        third.aceHigh = true;
        System.out.println(first.toString()+" ; "+third.toString()+" ; "+second.toString());
        if (third.sameNumber(first) || third.sameNumber(second)) {
            if (this.user_balance - (bet * 2) < 0) {
                System.out.println("You have lost the rest of your money.");
                this.pot += this.user_balance;
                this.user_balance = 0.0;
                System.out.println("Your balance is now $"+user_balance+".");
                System.out.println("The pot is now $"+this.pot+".");
                return;
            }
            System.out.println("Same card, so you pay double your bet.");
            this.user_balance -= bet * 2;
            this.pot += bet * 2;
            System.out.println("Your balance is now $"+user_balance+".");
            System.out.println("The pot is now $"+this.pot+".");
            return;
        }
        if (third.getValue() > Math.min(first.getValue(), second.getValue()) &&
                third.getValue() < Math.max(first.getValue(), second.getValue())) {
            System.out.println("In Between! You have won $"+bet+".");
            this.user_balance += bet;
            this.pot -= bet;
        }
        else {
            System.out.println("Not in between, so you lose $"+bet+".");
            this.user_balance -= bet;
            this.pot += bet;
        }
        System.out.println("Your balance is now $"+user_balance+".");
        System.out.println("The pot is now $"+this.pot+".");
    }

    public void nextTurn() {
        if (this.turn == this.num_players - 1) {
            this.turn = 0;
            return;
        }
        this.turn++;
    }

    public void play() {
        System.out.println("Player "+this.turn+" is going to start.");
        int num_turns = 0;
        while(this.pot > 0) {
            if (num_turns == this.num_players) {
                this.first_turn_switch = false;
            }
            if (this.turn == 0) {
                this.userTurn();
            }
            else {
                this.cpuTurn(this.turn);
            }
            this.nextTurn();
            System.out.println("\n");
            GameTimer.wait(5000);
            num_turns++;
        }
        System.out.println("Game over. Final balances:");
        System.out.println("You: " + this.user_balance);
        for (int i = 1; i < this.num_players ; i++) {
            System.out.println("Player "+i+": " + this.players[i].balance);
        }
    }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        System.out.println("How many players do you want to play against?");
        int num = Integer.parseInt(read.nextLine());
        System.out.println("What do you want the buy-in to be?");
        double buy = Double.parseDouble(read.nextLine());
        System.out.println("How much money do you want everyone to start with?");
        double bal = Double.parseDouble(read.nextLine());
        InBetween game = new InBetween(num + 1, buy, bal);
        game.play();
    }
}
