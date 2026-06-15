package org.example.service;

import java.util.Date;
import java.util.List;

public interface BillingService {
  void list();
  void cashIn(long amount);
  void listBill();
  void pay(List<Integer> billIds);
  void dueDate();
  void schedule(int billId, Date date);
  void processDueSchedules();
  void listPayment();
  void searchBillByProvider(String provider);
}
