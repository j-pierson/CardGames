import java.util.*;

public class GoFish {

    private final Scanner scanner = new Scanner(System.in);
    public Random RANDOM = new Random();
    public int num_players;
    public int turn;
    public int[] scoreboard;
    public int tot_score = 0;
    public CardStack[] hands;
    public Deck deck;

    public GoFish(int players) {
        this.num_players = players;
        this.scoreboard = new int[this.num_players];
        this.deck = new Deck();
        this.deck.shuffle();
        this.hands = new CardStack[this.num_players];
        for (int i = 0 ; i < this.num_players ; i++) {
            hands[i] = new CardStack();
        }
        this.turn = RANDOM.nextInt(this.num_players);
    }

    public void deal() {
        int cards_to_deal;
        if (this.num_players > 3) {
            cards_to_deal = 5;
        }
        else cards_to_deal = 7;
        for (int i = 0; i < cards_to_deal; i++) {
            for (int j = 0; j < this.num_players; j++) {
                this.hands[j].put(this.deck.draw());
            }
        }
    }

    public String makeCpuGuess(int player) {
        Hashtable<String, Integer> card_counts = new Hashtable<>();
        Card[] player_cards = this.hands[player].getCards();
        for (Card player_card : player_cards) {
            String number = player_card.num;
            if (card_counts.containsKey(number)) {
                card_counts.put((number), card_counts.get(number) + 1);
            }
            else {
                card_counts.put(number, 1);
            }
        }
        String guess = null;
        int max_val = 0;
        for (String card : card_counts.keySet()) {
            if (card_counts.get(card) > max_val) {
                guess = card;
                max_val = card_counts.get(card);
            }
        }
        return guess;
    }

    public int makeCpuResponse(int asker, int player, String given_number)  {
        int result = 0;
        for (Card card : this.hands[player].getCards()) {
            if (card.num.equals(given_number)) {
                this.hands[player].remove(card);
                this.hands[asker].put(card);
                result++;
            }
        }
        if (this.hands[player].size == 0) {
            if (this.deck.size() > 0) {
                System.out.println("Player "+player+" is out of cards, so they draw.");
                this.hands[player].put(this.deck.draw());
            }
        }
        return result;
    }

    public void makeCpuTurn(int player)  {
        System.out.println("It is now Player "+player+"'s turn.");
        if (this.hands[player].size == 0) {
            System.out.println("Player "+player+" is out of cards.");
            this.nextTurn();
            return;
        }
        String guess = makeCpuGuess(player);
        int ask_player = RANDOM.nextInt(this.num_players);
        while (ask_player == player || this.hands[ask_player].size == 0) {
            ask_player = RANDOM.nextInt(this.num_players);
        }
        System.out.println("Player "+player+" asks Player "+ask_player+" for "+guess+"s.");
        
        int turn_result;
        if (ask_player != 0) {
            turn_result = makeCpuResponse(player, ask_player, guess);
        }
        else {
            turn_result = makeUserResponse(player, guess);
        }
        if (turn_result == 0) {
            System.out.println("Player "+ask_player+" says Go Fish.");
            if (this.deck.size() > 0) {
                Card drawn = this.deck.draw();
                this.hands[player].put(drawn);
                this.checkForSets(player, drawn.num);
                if (this.deck.size() == 0) {
                    System.out.println("The deck is now empty, so no more drawing.");
                }
            }
        }
        else {
            System.out.println("Player "+ask_player+" gave "+turn_result+" "+guess+"s.");
            this.checkForSets(player, guess);
        }
        this.nextTurn();
    }

    public void makeUserTurn()  {
        System.out.println("It is your turn now. Here is your hand: " + this.hands[0].toString());
        System.out.println("What card would you like to ask for?");
        String card = scanner.nextLine();
        System.out.println("What player do you want to ask from?");
        int ask_player = Integer.parseInt(scanner.nextLine());
        int response = makeCpuResponse(0, ask_player, card);
        if (response == 0) {
            System.out.println("Player "+ask_player+" says Go Fish.");
            if (this.deck.size() > 0) {
                Card drawn = this.deck.draw();
                System.out.println("You have drawn a " + drawn + ".");
                this.hands[0].put(drawn);
                this.checkForSets(0, drawn.num);
                if (this.deck.size() == 0) {
                    System.out.println("The deck is now empty, so no more drawing.");
                }
                System.out.println("Here is your new hand: " + this.hands[0].toString());
            }
        }
        else {
            System.out.println("Player "+ask_player+" has give you "+response+" "+card+"s!");
            this.checkForSets(0, card);
            System.out.println("Here is your new hand: " + this.hands[0].toString());
            
        }
        this.nextTurn();
    }

    public int makeUserResponse(int asker, String guess)  {
        System.out.println("Here is your hand: " + this.hands[0].toString());
        int result = 0;
        for (Card card : this.hands[0].getCards()) {
            if (card.num.equals(guess)) {
                this.hands[0].remove(card);
                this.hands[asker].put(card);
                result++;
            }
        }
        System.out.println("How many "+guess+"s can you give to Player "+asker+"?");
        int given = Integer.parseInt(scanner.nextLine());
        while (given != result) {
            System.out.println("Check again. How many "+guess+"s can you give to Player "+asker+"?");
            given = Integer.parseInt(scanner.nextLine());
        }
        if (this.hands[0].size == 0) {
            if (this.deck.size() > 0) {
                System.out.println("You are out of cards, so you draw one.");
                
                this.hands[0].put(this.deck.draw());
                System.out.println("Here is your new hand: " + this.hands[0].toString());
            }
        }
        return given;
    }

    public void nextTurn() {
        if (this.turn == this.num_players - 1) {
            this.turn = 0;
        }
        else {
            this.turn++;
        }
    }

    public void checkForSets(int player, String given_card)  {
        int count = 0;
        Card[] cards = new Card[4];
        for (Card card : this.hands[player].getCards()) {
            if (card.num.equals(given_card)) {
                cards[count] = card;
                count++;
            }
        }
        if (count == 4) {
            for (Card card : cards) {
                this.hands[player].remove(card);
            }
            System.out.println("Player "+player+" made a set of "+given_card+"s!");
            this.scoreboard[player]++;
            this.tot_score++;
            this.display_scoreboard();
        }
    }

    public void display_scoreboard()  {
        System.out.println("CURRENT SCOREBOARD:");
        for (int i = 0; i < this.num_players; i++) {
            System.out.println("Player "+i+" has "+this.scoreboard[i]+" points.");
        }
        
    }

    public void play()  {
        System.out.println("Dealing cards...");
        this.deal();
        System.out.println("Your hand: " + this.hands[0].toString());
        if (this.turn == 0) {
            System.out.println("Player "+this.turn+"(you) is going to start.");
        }
        else {
            System.out.println("Player "+this.turn+" is going to start.");
        }
        System.out.println("\n");
        while (this.tot_score < 13) {
            if (this.turn != 0) {
                this.makeCpuTurn(this.turn);
            }
            else {
                this.makeUserTurn();
            }
            System.out.println("\n");
            GameTimer.wait(3000);
        }
        System.out.println("Game Over.");
    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("How many players would you like to play against(max 9)?");
        int num = Integer.parseInt(reader.nextLine());
        System.out.println("OK. Creating a game with "+(num + 1)+" players.");
        GoFish game = new GoFish(num + 1);
        game.play();
    }
}
