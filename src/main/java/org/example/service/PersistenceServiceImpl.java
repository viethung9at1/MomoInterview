package org.example.service;

import org.example.AppConstant;
import org.example.Memory;
import org.example.dto.Bill;
import org.example.dto.Payment;
import org.example.dto.ScheduledPayment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PersistenceServiceImpl implements PersistenceService {

  @Override
  public void saveBillData() {
    try {
      List<String> lines = new ArrayList<>();
      for (Bill b : Memory.BILLS.values()) {
        lines.add(String.format("%d,%s,%d,%s,%s,%s",
                b.getBillId(), b.getBillType(), b.getAmount(),
                new SimpleDateFormat(AppConstant.DD_MM_YYYY).format(b.getDueDate()),
                b.getBillState(), b.getProvider()));
      }
      Files.write(Paths.get(AppConstant.BILL_DATA_FILE_NAME), lines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.err.println("Error writing bill data: " + e.getMessage());
    }
  }

  @Override
  public void saveCashData() {
    try {
      Files.write(Paths.get(AppConstant.CASH_DATA_FILE_NAME),
              List.of(String.valueOf(Memory.CURRENT_CASH)), StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.err.println("Error writing cash data: " + e.getMessage());
    }
  }

  @Override
  public void savePaymentData() {
    try {
      List<String> lines = new ArrayList<>();
      for (Payment p : Memory.PAYMENTS.values()) {
        lines.add(String.format("%d,%s,%s,%d",
                p.getPaymentId(),
                new SimpleDateFormat(AppConstant.DD_MM_YYYY).format(p.getPaymentDate()),
                p.getPaymentState(),
                p.getBillId()));
      }
      Files.write(Paths.get(AppConstant.PAYMENT_DATA_FILE_NAME), lines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.err.println("Error writing payment data: " + e.getMessage());
    }
  }

  @Override
  public void saveScheduleData() {
    try {
      List<String> lines = new ArrayList<>();
      for (ScheduledPayment s : Memory.SCHEDULES) {
        lines.add(String.format("%d,%s", s.getBillId(),
                new SimpleDateFormat(AppConstant.DD_MM_YYYY).format(s.getScheduledDate())));
      }
      Files.write(Paths.get(AppConstant.SCHEDULE_DATA_FILE_NAME), lines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.err.println("Error saving schedule data: " + e.getMessage());
    }
  }
}
