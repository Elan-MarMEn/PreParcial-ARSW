package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

@Service("MoneyLaunderingServiceStub")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {

   private Hashtable<String,SuspectAccount> suspectAccounts;

    public MoneyLaunderingServiceStub() {
        this.suspectAccounts = new Hashtable<>();
        suspectAccounts.put("LA",new SuspectAccount("LA",1541));
        suspectAccounts.put("LE",new SuspectAccount("LE",1541));
        suspectAccounts.put("LI",new SuspectAccount("LI",1541));
        suspectAccounts.put("LO",new SuspectAccount("LO",1541));
        suspectAccounts.put("LU",new SuspectAccount("LU",1541));
    }


    @Override
    public void updateAccountStatus(String accountId,SuspectAccount suspectAccount)throws MoneyLaunderingServiceException{
        if (suspectAccounts.get(accountId)==null){
            throw new MoneyLaunderingServiceException("accountId not exists");
        }else {
            suspectAccounts.get(accountId).setAmountOfSmallTransactions(suspectAccount.amountOfSmallTransactions);
        }
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId)throws MoneyLaunderingServiceException {
        if(suspectAccounts.get(accountId)==null){
            throw new MoneyLaunderingServiceException("accountId not exists");
        }else {
            return suspectAccounts.get(accountId);
        }
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        List<SuspectAccount> suspectAccountArrayList= new ArrayList<>();
        for(String s : suspectAccounts.keySet()){
            suspectAccountArrayList.add(suspectAccounts.get(s));
        }
        return suspectAccountArrayList;
    }

    @Override
    public void addAccount(SuspectAccount suspectAccount) throws MoneyLaunderingServiceException {
        if (suspectAccounts.get(suspectAccount.accountId)==null){
            suspectAccounts.put(suspectAccount.accountId,suspectAccount);
        }else{
            throw new MoneyLaunderingServiceException("Account already exist");
        }
    }
}
