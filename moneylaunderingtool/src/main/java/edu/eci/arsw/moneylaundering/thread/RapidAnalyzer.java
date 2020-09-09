package edu.eci.arsw.moneylaundering.thread;

import edu.eci.arsw.moneylaundering.Transaction;
import edu.eci.arsw.moneylaundering.TransactionAnalyzer;
import edu.eci.arsw.moneylaundering.TransactionReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RapidAnalyzer  implements Runnable{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int valueA, valueB;
    private List<File> transactionList;
    private AtomicInteger amountOfFilesProcessed;
    private ArrayList<Integer> pause;

    private List<RapidAnalyzerTransactions> rapidAnalyzerTransactions;
    private Thread t;

    public RapidAnalyzer(TransactionAnalyzer transactionAnalyzer, int valueA, int valueB, List<File> transactionList,TransactionReader transactionReader,AtomicInteger amountOfFilesProcessed,ArrayList<Integer> pause) {
        this.transactionAnalyzer=transactionAnalyzer;
        this.transactionReader=transactionReader;
        this.valueA=valueA;
        this.valueB=valueB;
        this.transactionList=transactionList;
        this.amountOfFilesProcessed=amountOfFilesProcessed;

        this.pause=pause;
        this.rapidAnalyzerTransactions=new ArrayList<>();
        t= new Thread(this,Integer.toString(valueA));
        t.start();
    }

    @Override
    public void run() {

        for (int i = valueA; i <valueB; i++) {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionList.get(i));
//            if(transactions.size()>10000){
//                setUp(transactions,1000);
//                amountOfFilesProcessed.incrementAndGet();
//            }else {
                for (Transaction transaction : transactions) {
                    transactionAnalyzer.addTransaction(transaction);
                    synchronized (pause){
                        if (pause.size()==0){
                            try {
                                pause.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                amountOfFilesProcessed.incrementAndGet();
//            }
        }

    }

    public void setUp(List<Transaction> transactions, int NUMBER_OF_THREADS){
        int ctd = transactions.size() / NUMBER_OF_THREADS;

        if (NUMBER_OF_THREADS % 2 == 0) {
            for (int i = 0; i < transactions.size(); i += ctd) {
                RapidAnalyzerTransactions t = new RapidAnalyzerTransactions(transactionAnalyzer, i, ctd + i, transactions);
                rapidAnalyzerTransactions.add(t);
            }
        } else {
            for (int i = 0; i < ctd * (NUMBER_OF_THREADS - 1); i += ctd) {
                RapidAnalyzerTransactions t = new RapidAnalyzerTransactions(transactionAnalyzer, i, ctd + i, transactions);
                rapidAnalyzerTransactions.add(t);
            }
            RapidAnalyzerTransactions t = new RapidAnalyzerTransactions(transactionAnalyzer, ctd * (NUMBER_OF_THREADS - 1), transactions.size(), transactions);
            rapidAnalyzerTransactions.add(t);
        }
    }
}
