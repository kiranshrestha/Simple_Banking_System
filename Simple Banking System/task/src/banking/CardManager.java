package banking;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class CardManager {

    Scanner s;
    private final CreditCardBuilder cardBuilder;
    private final DatabaseHelper dbHelper;
    
    public CardManager(Scanner s, DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.s = s;
        cardBuilder = new CreditCardBuilder();
    }

    public void showMenu() {
        System.out.println();
        System.out.println(
                "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");

        int input = s.nextInt();
        s.nextLine();
        System.out.println();
        switch (input) {
            case 1 : {
                createAnAccount();
                showMenu();
                break;
            }
            case 2 : {
                loginToAccount();
                showMenu();
                break;
            }
            case 0 : {
                System.out.println("Bye!");
                System.exit(0);
            }
        }

    }

    private void loginToAccount() {
        System.out.println("Enter your card number:");
        String cardNo = s.nextLine();
        System.out.println("Enter your PIN:");
        int cardPin = s.nextInt();
        s.nextLine();
        Card card = new Card(cardNo, cardPin);
        Card rs = dbHelper.checkLogin(card);
        System.out.println();
        if(rs != null ) {
            System.out.println("You have successfully logged in!");
            showLoggedInUserMenu(card);
        }
        else {
            System.out.println("Wrong card number or PIN!");
        }

        System.out.println();

    }

    private void showLoggedInUserMenu(Card card) {
        System.out.println();
        System.out.println(
                "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
        int input = s.nextInt();
        s.nextLine();
        System.out.println();
        switch (input) {
            case 1 : {
                System.out.printf("Balance: %d\n", card.balance);
                showLoggedInUserMenu(card);
                break;
            }
            case 2 : {
                System.out.println("Enter income:");
                card.balance = s.nextLong();
                s.nextLine();
                dbHelper.updateIncome(card);
                showLoggedInUserMenu(card);
                break;
            }
            case 3 : {
                System.out.println("Transfer\n" +
                        "Enter card number:");
                String cardNo = s.nextLine();
                CreditCardBuilder cardBuilder = new CreditCardBuilder();
                if(cardBuilder.isCardValid(cardNo)) {
                    if (dbHelper.checkIfCardExists(cardNo)) {
                        System.out.println("Enter how much money you want to transfer:");
                        long amountToTransfer = s.nextLong();
                        if (dbHelper.checkSufficientBalanceToTransfer(card, amountToTransfer)) {
                            dbHelper.beginTransaction(card, cardNo, amountToTransfer);
                            System.out.println("Success!");
                        } else {
                            System.out.println("Not enough money!");
                        }

                    } else {
                        System.out.println("Such a card does not exist.");
                    }
                } else {
                    System.out.println("Probably you made a mistake in the card number. Please try again!");
                }
                showLoggedInUserMenu(card);
                break;
            }
            case 4 : {
                if (dbHelper.closeAccount(card)) {
                    System.out.println("The account has been closed!");
                }
            }
            case 5 : {
                System.out.println("You have successfully logged out!");
                showMenu();
                break;
            }
            case 0 : {
                System.out.println("Bye!");
                System.exit(0);
            }
        }
    }



    public void createAnAccount() {
        Card card = generateCard();
        System.out.printf("Your card has been created\n" +
                "Your card number:\n" +
                "%S\n" +
                "Your card PIN:\n" +
                "%d\n", card.cardNumber, card.pin );
        dbHelper.createNewAccount(card);
    }

    Card generateCard() {
        int pin = getRandomPin();
        int size = dbHelper.getTotalNoOfCards();
        String cardNumber = cardBuilder.generateCardNumber(CreditCardBuilder.CARD_IDENTIFIER.add(BigInteger.valueOf(size)).toString());
        return new Card(cardNumber, pin);
    }

    private int getRandomPin() {
        Random r = new Random();
        return r.nextInt(10000 - 1000) + 1000;
    }


}
