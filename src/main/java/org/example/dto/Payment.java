package org.example.dto;

import org.example.enums.PaymentState;

import java.util.Date;

public class Payment {
  int paymentId;
  Date paymentDate;
  PaymentState paymentState;
  int billId;

  public Payment(int paymentId, Date paymentDate, PaymentState paymentState, int billId) {
    this.paymentId = paymentId;
    this.paymentDate = paymentDate;
    this.paymentState = paymentState;
    this.billId = billId;
  }

  public int getPaymentId() { return paymentId; }
  public Date getPaymentDate() { return paymentDate; }
  public PaymentState getPaymentState() { return paymentState; }
  public int getBillId() { return billId; }

  public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
  public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
  public void setPaymentState(PaymentState paymentState) { this.paymentState = paymentState; }
  public void setBillId(int billId) { this.billId = billId; }

  @Override
  public String toString() {
    return "Payment{" +
            "paymentId=" + paymentId +
            ", paymentDate=" + paymentDate +
            ", paymentState=" + paymentState +
            ", billId=" + billId +
            '}';
  }
}
