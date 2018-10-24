import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Scanner;

public class Run{

  public static void main(String[] args) throws IOException {
    Scanner sc = new Scanner(System.in);
    String initialInput ="";
    do {
      System.out.println("Would you like to validate a blockchain (v), make a transaction (t), or quit (q)");
      System.out.print("(Keep in mind that making a transaction automatically validates a blockchain): ");
      initialInput = sc.nextLine();
      if (initialInput.equalsIgnoreCase("q"))
        break;
      if (initialInput.equalsIgnoreCase("v")) {
        String fileName = "";
        System.out.print("please enter the blockchain you would like to read, ex: bitcoinBank.txt ");
        fileName = sc.nextLine(); // "bitcoinBank.txt"
        if (!fileName.contains(".txt")) {
          System.err.println("THE FILE MUST BE END IN .txt");
          System.out.print("please try again... ");
          fileName = sc.nextLine(); // "bitcoinBank.txt"
        }

        BlockChain bc;
        bc = BlockChain.fromFile(fileName); // importing the blockchain data file

        boolean isValid = bc.validateBlockchain(fileName);
        System.out.println("The blockchain valid: " + isValid); // validation of the blockchain data.
      }else if (initialInput.equalsIgnoreCase("t")) {

        String fileName = "";
        System.out.print("please enter the blockchain you would like to read, ex: bitcoinBank.txt ");
        fileName = sc.nextLine(); // "bitcoinBank.txt"
        if (!fileName.contains(".txt")) {
          System.err.println("THE FILE MUST BE END IN .txt");
          System.out.print("please try again... ");
          fileName = sc.nextLine(); // "bitcoinBank.txt"
        }

        BlockChain bc;
        bc = BlockChain.fromFile(fileName); // importing the blockchain data file
        boolean isValid = bc.validateBlockchain();
        if (isValid) {
          System.out.println("The blockchain valid: " + isValid); // validation of the blockchain data.
          String anotherTransaction; // used for checking if the user wants to create another transaction.
          System.out.print("Would you like to continue and make a transaction? (yes || no) ");
          anotherTransaction = sc.nextLine();
          while (anotherTransaction.equalsIgnoreCase("yes")) { // runs until the user says no
            System.out.print("Please enter the sender name: ");
            String sender = "";
            sender = sc.nextLine();
            if (sender.equals("bitcoin")) {
              System.err.println("You cannot use this username, nor send bitcoin to this username.");
              System.out.print("Please enter a new username: ");
              sender = sc.nextLine();
            }
            System.out.println("Your current balance: " + bc.getBalance(sender));
            if (bc.getBalance(sender) >= 0) { // if the sending user has a balance greater than or equal 0, allow them to send money.
              System.out.print("Please enter the receiver name: ");
              String receiver = "";
              receiver = sc.nextLine();
              if (receiver.equals("bitcoin")) {
                System.err.println("You cannot use this username, nor send bitcoin to this username.");
                System.out.print("Please enter a new username: ");
                receiver = sc.nextLine();
              }
              System.out.print("Please enter the amount: ");
              int amount = Integer.parseInt(sc.nextLine());
              if (amount <= bc.getBalance(sender)) { // checks if the sending user has sufficient funds.
                System.out.println("sending...");
                int index = bc.blockchain.size(); // new index (end of the blockchain)
                Timestamp timestamp = new Timestamp(System.currentTimeMillis()); // current timestamp
                Transaction transaction = new Transaction(sender, receiver, amount);
                String previousHash = bc.blockchain.get(bc.blockchain.size() - 1).getPreviousHash(); //temp hash
                boolean foundAHashedValueStartingWith5Leading0s = false;
                String hashedMe = "";
                int hashTrials = 0;
                String nonce = "";
                /* nonce algorithm */
                do { // lets find a proper nonce :) by generating a string in between 3-20 characters.
                  nonce = generateString(); // runs the generateString method to generate a nonce in between 3-20 characters.
                  Block b = new Block(index, timestamp, transaction, nonce, previousHash, previousHash); // creates a temporary block to check for leading 0's
                  hashedMe = Sha1.hash(b.toString()); // magic step
                  hashTrials++; // used for statistics in report
                  foundAHashedValueStartingWith5Leading0s = (hashedMe.startsWith("00000")); // when a hashed value is found with 5 leading 0s, the loop will exit.
                } while (foundAHashedValueStartingWith5Leading0s == false);
                System.out.println(hashedMe + ": only took me " + hashTrials + " Times!"); // statistical purposes
                Block b = new Block(index, timestamp, transaction, nonce, previousHash, hashedMe);
                bc.blockchain.add(b);
                System.out.println("Sent!");
                System.out.println("Would you like to add another transaction? (yes, no)");
                anotherTransaction = sc.nextLine();
              } else {
                System.err.println("Sorry, you cannot send more bitcoins than you have.");
              }
            } else {
              System.err.println("Sorry, you have no bitcoins to send.");
            }
          }

          // after the program has finished, write to a new file.
          bc.toFile("testtt.txt");
        }else
          System.out.println("The blockchain is not valid, please try again...");
      } else {
        System.err.println("The entered input was invalid, please enter another input.");
      }
    }while(true);
  }

  /**
   * Generate a nonce string in between leftLimit: 33, and rightLimit: 126 -- using StringBuilder and Random in between 3-20 characters.
   * @return nonce string.
   */
  private static String generateString(){
    int leftLimit = 33; // letter 'a'
    int rightLimit = 126; // letter 'z'
    int targetStringLength = 20 - (new Random().nextInt((17 - 0) + 1) + 0);
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
      int randomLimitedInt = leftLimit + (int)
              (random.nextFloat() * (rightLimit - leftLimit + 1));
      buffer.append((char) randomLimitedInt);
    }
    String generatedString = buffer.toString();

    return generatedString;
  }

}
