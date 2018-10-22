import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BlockChain{

  /**
   * global blockchain arraylist, containing blocks.
   */
  public static ArrayList<Block> blockchain = new ArrayList<Block>();

  /**
   * Initialize the instance of blockchain in the BlockChain class.
   * @param blockchain BlockChain instance.
   */
  public BlockChain(ArrayList<Block> blockchain){
    this.blockchain = blockchain;
  }

  /**
   * Imports transaction and blockchain data from a file and adds this data to the blockchain arraylist.
   * @param fileName name of the blockchain data file.
   * @return an instance of the newly created blockchain.
   * @throws IOException Incase there is an error during the initialization process, this method will throw an input/output exception.
   */
  public static BlockChain fromFile(String fileName) throws IOException {
    // Finds the data file in the /src/ folder
    Scanner fileIn = new Scanner(new File(System.getProperty("user.dir") + "/src/" + fileName));
    // reads through the lines of the textfile to populate the blockchain.
    blockchain = new ArrayList<Block>();
    while(fileIn.hasNextLine()){
      int index = Integer.parseInt(fileIn.nextLine());
      Timestamp timestamp = new Timestamp(Long.parseLong(fileIn.nextLine()));
      Transaction transaction = new Transaction(fileIn.nextLine(), fileIn.nextLine(), Integer.parseInt(fileIn.nextLine()));
      String nonce = fileIn.nextLine();
      String previousHash = fileIn.nextLine();
      if(index == 0)
        blockchain.add(new Block(index,timestamp,transaction,nonce,"00000", previousHash));
      else
        blockchain.add(new Block(index,timestamp,transaction,nonce,blockchain.get(index-1).getExpectedHash(), previousHash));


    }
    // clean up
    fileIn.close();
    return new BlockChain(blockchain);
  }


  /**
   * Writes the current instance of blockchain to a new file. (history of transactions).
   * File must be located in src folder to be found.
   * @param fileName filename of the banking transaction file.
   * @throws IOException execption called when the input/output throws an error.
   */
  public void toFile(String fileName) throws IOException{
    // write all of the values of each block in the blockchain to a textfile using Bufferedwriter
    BufferedWriter outputWriter = null;
    outputWriter = new BufferedWriter(new FileWriter(fileName));
    for (Block b : blockchain){
      outputWriter.write(Integer.toString(b.getIndex()));
      outputWriter.newLine();
      outputWriter.write(Long.toString(b.getTimestamp().getTime()));
      outputWriter.newLine();
      outputWriter.write(b.getTransaction().getSender());
      outputWriter.newLine();
      outputWriter.write(b.getTransaction().getReceiver());
      outputWriter.newLine();
      outputWriter.write(Integer.toString(b.getTransaction().getAmount()));
      outputWriter.newLine();
      outputWriter.write(b.getNonce());
      outputWriter.newLine();
      outputWriter.write(b.getExpectedHash());
      outputWriter.newLine();
    }
    // clean up the writer
    outputWriter.flush();
    outputWriter.close();
  }

  /**
   * Validate the blockchain for transactions by comparing the calculated hash with the registered hash.
   * No output verification file.
   * @return the validation of the blockchain.
   */
  public boolean validateBlockchain() {
    Block block, prevBlock;
    // looping through the blockchain
    for(int i=1; i < blockchain.size(); i++) {
      block = blockchain.get(i);
      prevBlock = blockchain.get(i-1);
      //comparing the calculated hash with the registered hash
      try {
        if(!block.getHash().equals(block.calculateHash()) ){
          System.err.println("these hashes are not equal");
          return false;
        }
        //compare previous hash and registered previous hash
        if(!prevBlock.getHash().equals(block.getPreviousHash()) ) {
          System.out.println("Previous Hashes not equal");
          return false;
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  /**
   * Validate the blockchain for transactions by comparing the calculated hash with the registered hash.
   * Output verification file (blockchain_x_mocon103.txt) where x is another miners id
   * @param fileName
   * @return
   */
  public boolean validateBlockchain(String fileName) {
    Block block, prevBlock;
    // looping through the blockchain
    for(int i=1; i < blockchain.size(); i++) {
      block = blockchain.get(i);
      prevBlock = blockchain.get(i-1);
      //comparing the calculated hash with the registered hash
      try {
        if(!block.getHash().equals(block.calculateHash()) ){
          System.err.println("these hashes are not equal");
          return false;
        }
        //compare previous hash and registered previous hash
        if(!prevBlock.getHash().equals(block.getPreviousHash()) ) {
          System.out.println("Previous Hashes not equal");
          return false;
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    try {
      this.toFile(fileName.replace(".txt", "_mocon103.txt"));
    } catch (IOException e) {
      System.err.println("Sorry, the file could not be saved! " + e);
    }
    return true;
  }

  /**
   * Finds the current balance for the user trying to send bitcoins.
   * @param username user that is trying to send bitcoins.
   * @return the bitcoin count of the user.
   */
  public int getBalance(String username) {
    int balance = 0;
    for (int i = 0; i<blockchain.size();i++){
      Transaction tnx = blockchain.get(i).getTransaction();
      String[] transactionString = tnx.toString().split("[:=]+");
      for (String a : transactionString){
        if (a.equals(username)){
          if (transactionString[1].equals(username))
            balance+= Integer.parseInt(transactionString[2]);
          else
            balance -= Integer.parseInt(transactionString[2]);
        }
      }
    }
    return balance;
  }

  /**
   * adding a block to the current blockchain.
   * @param block the block that is to be added.
   */
  public void add(Block block) {
    blockchain.add(block);
    
  }

}
