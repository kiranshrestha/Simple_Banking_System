package banking;

public class Card {
    String cardNumber;
    int pin;
    long balance = 0;

    public Card(String cardNumber, int pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", pin=" + pin +
                ", balance=" + balance +
                '}';
    }
}
