package org.example.service;

import org.example.Memory;
import org.example.dto.Bill;
import org.example.enums.BillState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BillingServiceImplTest {
  @Test
  void cashIn_success() {
    // given
    PersistenceServiceImpl persistenceService = Mockito.mock(PersistenceServiceImpl.class);
    BillingServiceImpl billingService = new BillingServiceImpl(persistenceService);

    Memory.CURRENT_CASH = 0;

    // when
    billingService.cashIn(100);

    // then
    assertEquals(100, Memory.CURRENT_CASH);
  }

  @Test
  void pay_success() {
    // given

    PersistenceServiceImpl persistenceService = Mockito.mock(PersistenceServiceImpl.class);
    BillingServiceImpl billingService = new BillingServiceImpl(persistenceService);
    Memory.CURRENT_CASH = 100;
    Bill bill = new Bill();
    bill.setBillId(1);
    bill.setBillState(BillState.NOT_PAID);
    bill.setBillType("Electricity");
    bill.setAmount(100);


    Memory.PAYMENTS = new HashMap<>();
    Memory.BILLS = Map.of(1, bill);

    // when
    billingService.pay(List.of(1));

    // then
    assertEquals(0, Memory.CURRENT_CASH);
  }
}