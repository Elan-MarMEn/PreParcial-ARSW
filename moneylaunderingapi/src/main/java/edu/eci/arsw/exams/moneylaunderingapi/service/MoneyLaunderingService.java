package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;

public interface MoneyLaunderingService {
    void updateAccountStatus(String accountId,SuspectAccount suspectAccount)throws MoneyLaunderingServiceException;
    SuspectAccount getAccountStatus(String accountId)throws MoneyLaunderingServiceException;
    List<SuspectAccount> getSuspectAccounts();
    void addAccount(SuspectAccount suspectAccount)throws MoneyLaunderingServiceException;
}
