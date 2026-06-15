package org.example.service;

import org.example.AppConstant;
import org.example.Memory;
import org.example.dto.Bill;
import org.example.dto.Payment;
import org.example.dto.ScheduledPayment;
import org.example.enums.BillState;
import org.example.enums.PaymentState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillingServiceImpl implements BillingService {

  private final PersistenceService persistence;

  public BillingServiceImpl(PersistenceService persistence) {
    this.persistence = persistence;
  }

  @Override
  public void list() {
    System.out.println(Memory.BILLS);
  }

  @Override
  public void cashIn(long amount) {
    Memory.CURRENT_CASH += amount;
    persistence.saveCashData();
  }

  @Override
  public void listBill() {
    for (Bill bill : Memory.BILLS.values()) {
      System.out.println(bill);
    }
  }

  @Override
  public void pay(List<Integer> billIds) {
    for (Integer billId : billIds) {
      Payment payment = null;
      for (Payment p : Memory.PAYMENTS.values()) {
        if (p.getBillId() == billId) {
          payment = p;
        }
      }
      if (payment == null) {
        payment = new Payment(Memory.PAYMENTS.size() + 1, new Date(), PaymentState.PENDING, billId);
      }

      Bill bill = Memory.BILLS.get(billId);
      if (bill == null) {
        System.err.println("Bill not found");
        return;
      }
      if (bill.getBillState() == BillState.PAID) {
        System.err.println("Bill already paid");
        return;
      }
      if (Memory.CURRENT_CASH < bill.getAmount()) {
        System.err.println("Not enough cash to pay the bill");
        return;
      }
      Memory.CURRENT_CASH -= bill.getAmount();
      bill.setBillState(BillState.PAID);
      payment.setPaymentState(PaymentState.PROCESSED);
      Memory.PAYMENTS.put(payment.getPaymentId(), payment);
      persistence.saveBillData();
      persistence.saveCashData();
      persistence.savePaymentData();
    }
  }

  @Override
  public void dueDate() {
    for (Bill bill : Memory.BILLS.values()) {
      if (bill.getBillState() == BillState.NOT_PAID) {
        System.out.println(bill);
      }
    }
  }

  @Override
  public void schedule(int billId, Date date) {
    Bill bill = Memory.BILLS.get(billId);
    if (bill == null) {
      System.err.println("Bill not found");
      return;
    }
    if (bill.getBillState() == BillState.PAID) {
      System.err.println("Bill already paid");
      return;
    }
    for (ScheduledPayment s : Memory.SCHEDULES) {
      if (s.getBillId() == billId) {
        System.err.println("Payment for bill id " + billId + " is already scheduled");
        return;
      }
    }
    Memory.SCHEDULES.add(new ScheduledPayment(billId, date));
    persistence.saveScheduleData();
    System.out.println("Payment for bill id " + billId + " is scheduled on "
            + new SimpleDateFormat(AppConstant.DD_MM_YYYY).format(date));
  }

  @Override
  public void processDueSchedules() {
    Date today = new Date();
    List<ScheduledPayment> toRemove = new ArrayList<>();
    for (ScheduledPayment s : Memory.SCHEDULES) {
      if (s.getScheduledDate().after(today)) continue;
      toRemove.add(s);
      Bill bill = Memory.BILLS.get(s.getBillId());
      if (bill == null || bill.getBillState() == BillState.PAID) continue;
      int nextId = Memory.PAYMENTS.size() + 1;
      if (Memory.CURRENT_CASH < bill.getAmount()) {
        Memory.PAYMENTS.put(nextId, new Payment(nextId, s.getScheduledDate(), PaymentState.PENDING, s.getBillId()));
      } else {
        Memory.CURRENT_CASH -= bill.getAmount();
        bill.setBillState(BillState.PAID);
        Memory.PAYMENTS.put(nextId, new Payment(nextId, s.getScheduledDate(), PaymentState.PROCESSED, s.getBillId()));
      }
    }
    if (toRemove.isEmpty()) return;
    Memory.SCHEDULES.removeAll(toRemove);
    persistence.saveScheduleData();
    persistence.saveBillData();
    persistence.saveCashData();
    persistence.savePaymentData();
  }

  @Override
  public void listPayment() {
    for (Payment payment : Memory.PAYMENTS.values()) {
      System.out.println(payment);
    }
  }

  @Override
  public void searchBillByProvider(String provider) {
    for (Bill bill : Memory.BILLS.values()) {
      if (bill.getProvider().equalsIgnoreCase(provider) && bill.getBillState() == BillState.NOT_PAID) {
        System.out.println(bill);
      }
    }
  }
}
