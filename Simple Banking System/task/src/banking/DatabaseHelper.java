package banking;

import java.sql.*;

public class DatabaseHelper {
    Connection connection;

    public DatabaseHelper(Connection connection) {
        this.connection = connection;
    }

    public void createTableIfNotExists() {
        //Statement Creation
        try (Statement statement = connection.createStatement()){
            //Statement execution

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS card (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n " +
                    "'number' TEXT,\n " +
                            "'pin' TEXT,\n " +
                            "'balance' INTEGER DEFAULT 0" +
                    ")");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createNewAccount(Card card) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO card ('number', 'pin', 'balance') VALUES " +
                        "(?,?,?)"
        )){
            statement.setString(1, card.cardNumber);
            statement.setString(2, String.valueOf(card.pin));
            statement.setLong(3, card.balance);
            statement.executeUpdate();

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Card checkLogin(Card card) {
        Card card1 = null;
        try (Statement statement = connection.createStatement()){
            //Statement execution
            try(ResultSet rs = statement.executeQuery("SELECT * FROM card WHERE number = '"+card.cardNumber+"' AND pin = '" + card.pin + "'" )) {
                if(rs.next())
                {
                    card1 = new Card(rs.getString(2), Integer.parseInt(rs.getString(3)));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return card1;
    }

    public int getTotalNoOfCards() {
        int count = 0;
        try (Statement statement = connection.createStatement()){
            //Statement execution
             ResultSet rs = statement.executeQuery(
                    "SELECT COUNT(*) FROM card" );
            rs.next();
            count = rs.getInt(1);
             /*count = rs.getInt(0);*/
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public void updateIncome(Card card) {
        try (Statement statement = connection.createStatement()){
            //Statement execution
            int rs = statement.executeUpdate(
                    "UPDATE card SET balance = (balance + " + card.balance + ") WHERE number = " + card.cardNumber);
            /*if(rs == 1) {
                System.out.println("Income updated");
            } else
                System.out.println("Income not Updated");*/
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkIfCardExists(String cardNo) {
        try (Statement statement = connection.createStatement()){
            //Statement execution
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM card WHERE number = " + cardNo );
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean checkSufficientBalanceToTransfer(Card card, long amountToTransfer) {
        try (Statement statement = connection.createStatement()){
            //Statement execution
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM card WHERE number = " + card.cardNumber + " AND balance > " + amountToTransfer);
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean closeAccount(Card card) {
        try (Statement statement = connection.createStatement()){
            //Statement execution
            int rs = statement.executeUpdate(
                    "DELETE FROM card WHERE number = " + card.cardNumber );
            return rs == 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void beginTransaction(Card card, String cardNo, long amountToTransfer) {
        try (Statement statement = connection.createStatement()){
            //Statement execution
            //Deduct balance from user account
            statement.executeUpdate("UPDATE card SET balance = (balance - " + amountToTransfer + ") WHERE number = " + card.cardNumber);
            //transfer to this account
            statement.executeQuery("UPDATE card SET balance = (balance + " + amountToTransfer + ") WHERE number = " + cardNo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
