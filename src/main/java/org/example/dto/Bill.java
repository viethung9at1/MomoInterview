package org.example.dto;

import org.example.enums.BillState;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Bill {
  int billId;
  String billType;
  long amount;
  Date dueDate;
  BillState billState;
  String provider;

  public Bill() {
  }

  public Bill(int billId, String billType, long amount, Date dueDate, BillState billState, String provider) {
    this.billId = billId;
    this.billType = billType;
    this.amount = amount;
    this.dueDate = dueDate;
    this.billState = billState;
    this.provider = provider;
  }

  public int getBillId() { return billId; }
  public String getBillType() { return billType; }
  public long getAmount() { return amount; }
  public Date getDueDate() { return dueDate; }
  public BillState getBillState() { return billState; }
  public String getProvider() { return provider; }

  public void setBillId(int billId) { this.billId = billId; }
  public void setBillType(String billType) { this.billType = billType; }
  public void setAmount(long amount) { this.amount = amount; }
  public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
  public void setBillState(BillState billState) { this.billState = billState; }
  public void setProvider(String provider) { this.provider = provider; }

  @Override
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    return "Bill{" +
            "billId=" + billId +
            ", billType='" + billType + '\'' +
            ", amount=" + amount +
            ", dueDate=" + (dueDate != null ? sdf.format(dueDate) : null) +
            ", billState=" + billState +
            ", provider='" + provider + '\'' +
            '}';
  }
}
