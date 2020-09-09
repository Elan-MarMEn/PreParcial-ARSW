package edu.eci.arsw.moneylaundering.thread;

import edu.eci.arsw.moneylaundering.Transaction;
import edu.eci.arsw.moneylaundering.TransactionAnalyzer;

import java.io.File;
import java.util.List;

public class RapidAnalyzerTransactions implements Runnable {

    private TransactionAnalyzer transactionAnalyzer;
    private int valueA, valueB;
    private List<Transaction> transactionList;

    public RapidAnalyzerTransactions(TransactionAnalyzer transactionAnalyzer, int valueA, int valueB, List<Transaction> transactionList) {
        this.transactionAnalyzer = transactionAnalyzer;
        this.valueA = valueA;
        this.valueB = valueB;
        this.transactionList = transactionList;
    }


    @Override
    public void run() {
        for (int i=valueA;i<valueB;i++){
            transactionAnalyzer.addTransaction(transactionList.get(i));
        }
    }
}
