import java.io.UnsupportedEncodingException;
import java.sql.*;


public class Block {

  /**
   * index of block in blockchain.
   */
  private int index;
  /**
   * timestamp of transaction, converted from long to Timestamp.
   */
  private Timestamp timestamp;
  /**
   * transaction object, containing sender name, receiver name, and amount sent in block.
   */
  private Transaction transaction;
  /**
   * nonce string, the string used for proof-of-work.
   */
  private String nonce;
  /**
   * expected hash, used for testing validation.
   */
  private String previousHash;
  /**
   * SHA-1 hash of the block's toString method.
   */
  private String hash;
  /**
   * Simply a variable used to hold the previous hash variables for output purposes.
   * (A dummy variable to make things easier)
   */
  private String expectedHash;

  /**
   * Called upon initialisation of a new block.
   * @param index index of the block in the blockchain
   * @param timestamp timestamp of the transaction
   * @param transaction object containing sender, receiver, and amount of bitcoins sent
   * @param nonce randomly generated string in between 3-20 characters. (the string to be discovered by random trial)
   * @param previousHash the discovered hash from the nonce.
   */
  public Block (int index, Timestamp timestamp, Transaction transaction, String nonce, String previousHash, String expectedHash) {
    this.index = index;
    this.transaction = transaction;
    this.nonce = nonce;
    if (index == 0)
      this.previousHash = "00000";
    else
      this.previousHash = previousHash;
    this.expectedHash = expectedHash;
    this.timestamp = timestamp;
    try {
      this.hash = calculateHash();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

  }

  /**
   * Calculate the SHA-1 hash value of the toString() method.
   * @return The SHA-1 hashed value.
   * @throws UnsupportedEncodingException Incase the string cannot be hashed.
   */
  public String calculateHash() throws UnsupportedEncodingException {
    String calculatedHash = Sha1.hash(toString());
    return calculatedHash;
  }


  /**
   * Create a string representation for the block.
   * @return Formatted string to represent block.
   */
  public String toString(){
    return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
  }


  /* GETTERS AND SETTERS */

  public int getIndex() {
    return index;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public String getNonce() {
    return nonce;
  }

  public String getPreviousHash() {
    return previousHash;
  }

  public String getHash() {
    return hash;
  }


  public String getExpectedHash() {
    return expectedHash;
  }

}
