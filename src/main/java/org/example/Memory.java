package org.example;

import org.example.dto.Bill;
import org.example.dto.Payment;
import org.example.dto.ScheduledPayment;
import org.example.enums.BillState;
import org.example.enums.PaymentState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.AppConstant.BILL_DATA_FILE_NAME;
import static org.example.AppConstant.CASH_DATA_FILE_NAME;
import static org.example.AppConstant.DD_MM_YYYY;

public class Memory {
  public static Map<Integer, Bill> BILLS;
  public static Map<Integer, Payment> PAYMENTS;
  public static long CURRENT_CASH;
  public static List<ScheduledPayment> SCHEDULES = new ArrayList<>();

  public static void loadBillData() {
    List<Bill> bills = new ArrayList<>();
    try {
      List<String> lines = Files.readAllLines(Paths.get(BILL_DATA_FILE_NAME), StandardCharsets.UTF_8);
      for (int  i = 0; i < lines.size(); i++) {
        String[] parts = lines.get(i).split(",");
        if (parts.length == 6) {
          int billId = Integer.parseInt(parts[0].trim());
          String billType = parts[1].trim();
          long amount = Long.parseLong(parts[2].trim());
          Date dueDate = new SimpleDateFormat(DD_MM_YYYY).parse(parts[3].trim());
          BillState billState = BillState.valueOf(parts[4].trim().toUpperCase());
          String provider = parts[5].trim();
          bills.add(new Bill(billId, billType, amount, dueDate, billState, provider));
        }
      }
    } catch (IOException | ParseException e) {
      System.err.println("Error loading data: " + e.getMessage());
    }
    Memory.BILLS = bills.stream().collect(Collectors.toMap(Bill::getBillId, bill -> bill));
  }

  public static void loadPaymentData() {
    List<Payment> payments = new ArrayList<>();
    try {
      List<String> lines = Files.readAllLines(Paths.get(AppConstant.PAYMENT_DATA_FILE_NAME), StandardCharsets.UTF_8);
      for (int i = 0; i < lines.size(); i++) {
        String[] parts = lines.get(i).split(",");
        if (parts.length == 4) {
          int paymentId = Integer.parseInt(parts[0].trim());
          Date paymentDate = new SimpleDateFormat(DD_MM_YYYY).parse(parts[1].trim());
          PaymentState paymentState = PaymentState.valueOf(parts[2].trim().toUpperCase());
          int billId = Integer.parseInt(parts[3].trim());
          payments.add(new Payment(paymentId, paymentDate, paymentState, billId));
        }
      }
    } catch (IOException | ParseException e) {
      System.err.println("Error loading data: " + e.getMessage());
    }
    Memory.PAYMENTS = payments.stream().collect(Collectors.toMap(Payment::getPaymentId, payment -> payment));
  }
  public static void loadScheduleData() {
    SCHEDULES = new ArrayList<>();
    try {
      List<String> lines = Files.readAllLines(Paths.get(AppConstant.SCHEDULE_DATA_FILE_NAME), StandardCharsets.UTF_8);
      for (String line : lines) {
        String[] parts = line.split(",");
        if (parts.length == 2) {
          int billId = Integer.parseInt(parts[0].trim());
          Date scheduledDate = new SimpleDateFormat(DD_MM_YYYY).parse(parts[1].trim());
          SCHEDULES.add(new ScheduledPayment(billId, scheduledDate));
        }
      }
    } catch (IOException | ParseException e) {
      System.err.println("Error loading schedule data: " + e.getMessage());
    }
  }

  public static void loadCashData() {
    long currentCash = 0;
    try {
      List<String> lines = Files.readAllLines(Paths.get(CASH_DATA_FILE_NAME), StandardCharsets.UTF_8);
      if (!lines.isEmpty()) {
        currentCash = Long.parseLong(lines.get(0).trim());
      }
    } catch (IOException e) {
      System.err.println("Error loading cash data: " + e.getMessage());
    }
    Memory.CURRENT_CASH = currentCash;
  }
}
