package com.baeldung.lss.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

  @Async
  public void asyncCall() {
    System.out.println("Inside async method");
  }
}
