package com.example.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ExampleResource {
  private final Logger log = LoggerFactory.getLogger(ExampleResource.class);
  private ExampleCounter counter;

  public ExampleResource() {}

  @Inject
  public ExampleResource(ExampleCounter counter) {
    this.counter = counter;
  }

  @GET
  public ExampleResponse get(
      @QueryParam("add") @DefaultValue("1") int add,
      @QueryParam("wait") @DefaultValue("0") int wait)
      throws InterruptedException {
    log.info("start request add={}, wait={}", add, wait);
    counter.add(add);
    log.info("counter before wait: {}", counter);
    TimeUnit.SECONDS.sleep(wait);
    log.info("counter after wait: {}", counter);
    log.info("end request add={}, wait={}", add, wait);

    var result = new ExampleResponse();
    result.setCounter(counter.getCounter());
    result.setHost(System.getenv("HOSTNAME"));
    return result;
  }

  @GET
  @Path("/current")
  public ExampleResponse getCurrent() {
    var result = new ExampleResponse();
    result.setCounter(counter.getCounter());
    result.setHost(System.getenv("HOSTNAME"));
    return result;
  }
}
