package com.example.bankinge2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
      plugin = {"pretty"},
      tags = "not @ignore",
      glue = {"com.booking.bankinge2e.stepdefs"},
      features = "classpath:features")
public class CucumberE2ETest {


}
