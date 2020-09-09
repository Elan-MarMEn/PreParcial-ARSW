package edu.eci.arsw.moneylaundering;

import edu.eci.arsw.moneylaundering.thread.RapidAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;


    private ArrayList<Integer> pause = new ArrayList<>();
    private int NUMBER_OF_THREADS=10;
    private ArrayList<RapidAnalyzer> threadList = new ArrayList<>();


    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        pause.add(1);
    }

    public void processTransactionData()
    {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        setUpThreads(transactionFiles);
    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public void setUpThreads(List<File> transactions){

        int ctd = transactions.size() / NUMBER_OF_THREADS;

        if (NUMBER_OF_THREADS % 2 == 0) {
            for (int i = 0; i < transactions.size(); i += ctd) {
                RapidAnalyzer t = new RapidAnalyzer(transactionAnalyzer, i, ctd + i, transactions,transactionReader,amountOfFilesProcessed,pause);
                threadList.add(t);
            }
        } else {
            for (int i = 0; i < ctd * (NUMBER_OF_THREADS - 1); i += ctd) {
                RapidAnalyzer t = new RapidAnalyzer(transactionAnalyzer, i, ctd + i, transactions,transactionReader,amountOfFilesProcessed,pause);
                threadList.add(t);
            }
            RapidAnalyzer t = new RapidAnalyzer(transactionAnalyzer, ctd * (NUMBER_OF_THREADS - 1), transactions.size(), transactions,transactionReader,amountOfFilesProcessed,pause);

            threadList.add(t);
        }

    }

    public void setPauseNull() {
        pause.clear();
    }

    public void setPause() {

        synchronized (pause){
            pause.add(1);
            pause.notifyAll();
        }
    }

    public static void main(String[] args)
    {
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        moneyLaundering.processTransactionData();
        while(moneyLaundering.getTransactionFileList().size()!= moneyLaundering.amountOfFilesProcessed.get())
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
                break;
            moneyLaundering.setPauseNull();
            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
            System.out.println("Paused...");
            Scanner scanner2 = new Scanner(System.in);
            String line2 = scanner2.nextLine();
            if(line2.contains("exit"))
                break;
            moneyLaundering.setPause();
            System.out.println("Despaused...");

        }
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);

    }



}
