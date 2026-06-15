package org.example.dto;

import java.util.Date;

public class ScheduledPayment {
  int billId;
  Date scheduledDate;

  public ScheduledPayment(int billId, Date scheduledDate) {
    this.billId = billId;
    this.scheduledDate = scheduledDate;
  }

  public int getBillId() { return billId; }
  public Date getScheduledDate() { return scheduledDate; }
}
