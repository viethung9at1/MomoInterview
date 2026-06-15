package org.example;

import org.example.enums.Command;
import org.example.service.BillingService;
import org.example.service.BillingServiceImpl;
import org.example.service.PersistenceService;
import org.example.service.PersistenceServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
  public static void main(String[] args) throws ParseException {
    Path path = Paths.get(AppConstant.BILL_DATA_FILE_NAME);
    if (!path.toFile().exists()) {
      try {
        Files.createFile(path);
      } catch (IOException e) {
        System.err.println("Error creating data file: " + e.getMessage());
        System.exit(1);
      }
    }
    Memory.loadBillData();
    Memory.loadCashData();
    Memory.loadPaymentData();
    Memory.loadScheduleData();

    PersistenceService persistence = new PersistenceServiceImpl();
    BillingService billingService = new BillingServiceImpl(persistence);

    billingService.processDueSchedules();

    if (args.length == 0) {
      System.out.println("No command provided");
      return;
    }
    Command command = Command.valueOf(args[0].toUpperCase());
    switch (command) {
      case SEARCH_BILL_BY_PROVIDER:
        billingService.searchBillByProvider(args[1]);
        break;
      case EXIT:
        System.exit(0);
        break;
      case LIST:
        billingService.list();
        break;
      case CASH_IN:
        billingService.cashIn(Long.parseLong(args[1]));
        break;
      case LIST_BILL:
        billingService.listBill();
        break;
      case PAY:
        List<Integer> billIds = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
          billIds.add(Integer.parseInt(args[i]));
        }
        billingService.pay(billIds);
        break;
      case DUE_DATE:
        billingService.dueDate();
        break;
      case SCHEDULE:
        int billId = Integer.parseInt(args[1]);
        Date date = new SimpleDateFormat(AppConstant.DD_MM_YYYY).parse(args[2]);
        billingService.schedule(billId, date);
        break;
      case LIST_PAYMENT:
        billingService.listPayment();
        break;
    }
  }
}
