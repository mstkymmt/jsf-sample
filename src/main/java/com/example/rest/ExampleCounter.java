package com.example.rest;

import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class ExampleCounter implements Serializable {
  private int counter = 0;

  public int getCounter() {
    return counter;
  }

  public void setCounter(int counter) {
    this.counter = counter;
  }

  public void add(int delta) {
    counter += delta;
  }

  public void clear() {
    counter = 0;
  }

  @Override
  public String toString() {
    return "ExampleCounter@"
        + Integer.toHexString(System.identityHashCode(this))
        + "{"
        + "counter="
        + counter
        + '}';
  }
}
