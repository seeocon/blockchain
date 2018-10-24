public class Transaction {

  /**
   * Username of user sending bitcoin.
   */
  private String sender;
  /**
   * Username of person receiving bitcoin.
   */
  private String receiver;
  /**
   * Amount of bitcoins being sent in transaction.
   */
  private int amount;


  /**
   * Initialization of the transaction object, used for tracking where bitcoin money is sent.
   * @param sender the username of the sender.
   * @param receiver the username of the person receiving the bitcoin.
   * @param amount the amount being sent.
   */
  public Transaction(String sender, String receiver, int amount){
    this.sender = sender;
    this.receiver = receiver;
    this.amount = amount;
  }

  /**
   * Organized output for the Transaction object
   * @return string, containing the sender's, receiver's usernames and the amount of bitcoins sent.
   */
  public String toString() {
    return sender + ":" + receiver + "=" + amount;
  }

  /* GETTERS AND SETTERS */

  public String getSender() {
    return sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public int getAmount() {
    return amount;
  }


}
