package banking;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreditCardBuilder {

    private int checkSum;
    public static final String BIN = "400000";
    public static final BigInteger CARD_IDENTIFIER = new BigInteger("451848624");

    public String generateCardNumber(String accountIdentifier) {
        String tempAccount = BIN.concat(accountIdentifier);
        checkSum = 0;
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < tempAccount.length(); i++) {
            int dig = Character.getNumericValue(tempAccount.charAt(i));
            if (i % 2 == 0) {
                int greaterThan9 = dig * 2;
                if (greaterThan9 > 9) {
                    greaterThan9 = greaterThan9 - 9;
                }
                arrayList.add(greaterThan9);
            } else {
                arrayList.add(dig);
            }
        }
        Optional<Integer> sumAccount = arrayList.stream().reduce((sum, num) -> sum += num);
        sumAccount.ifPresent(integer -> {
            int cSum = integer % 10;
            checkSum = (cSum == 0) ? 0 : (10 - cSum);
        });
        return BIN.concat(accountIdentifier)
                .concat(String.valueOf(checkSum));
    }

    public boolean isCardValid(String cardNo) {
        String tempAccount = cardNo.substring(0, cardNo.length() -1);
        ArrayList<Integer> arrList = new ArrayList<>();
        for (int i = 0; i < tempAccount.length(); i++) {
            int dig = Character.getNumericValue(tempAccount.charAt(i));
            if (i % 2 == 0) {
                int greaterThan9 = dig * 2;
                if (greaterThan9 > 9) {
                    greaterThan9 = greaterThan9 - 9;
                }
                arrList.add(greaterThan9);
            } else {
                arrList.add(dig);
            }
        }
        Optional<Integer> sumAccount = arrList.stream().reduce((sum, num) -> sum += num);
        sumAccount.ifPresent(integer -> {
            int cSum = integer % 10;
            checkSum = (cSum == 0) ? 0 : (10 - cSum);
        });
        return checkSum == Character.getNumericValue(cardNo.charAt(cardNo.length() - 1));
    }


}
