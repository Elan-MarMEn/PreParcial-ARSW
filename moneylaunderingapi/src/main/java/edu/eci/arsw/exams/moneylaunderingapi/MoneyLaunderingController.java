package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MoneyLaunderingController
{
    @Autowired
    @Qualifier("MoneyLaunderingServiceStub")
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping( value = "/fraud-bank-accounts", method = RequestMethod.GET)
    public List<SuspectAccount> offendingAccounts() {
        return moneyLaunderingService.getSuspectAccounts();
    }


    @RequestMapping(value = "/fraud-bank-accounts",method = RequestMethod.POST)
    public ResponseEntity<?> addAccount(@RequestBody SuspectAccount suspectAccount){
        try {
            moneyLaunderingService.addAccount(suspectAccount);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingServiceException ex) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/fraud-bank-accounts/{accountId}",method = RequestMethod.GET)
    public ResponseEntity<?> getAccountStatus(@PathVariable String accountId){
        try {
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.ACCEPTED);
        } catch (MoneyLaunderingServiceException ex) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/fraud-bank-accounts/{accountId}",method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccountStatus(@PathVariable String accountId,@RequestBody SuspectAccount suspectAccount){
        try {
            moneyLaunderingService.updateAccountStatus(accountId,suspectAccount);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (MoneyLaunderingServiceException ex) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
}
