package com.example.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/direct")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DirectResource {
  private final Logger log = LoggerFactory.getLogger(DirectResource.class);

  @GET
  public ExampleResponse get(
      @QueryParam("add") @DefaultValue("1") int add,
      @QueryParam("wait") @DefaultValue("0") int wait,
      @Context HttpServletRequest request)
      throws InterruptedException {

    log.info("start request add={}, wait={}", add, wait);

    var session = request.getSession(true);
    Integer counter = (Integer) session.getAttribute("counter");
    counter = counter == null ? 1 : counter + add;

    log.info("counter before wait: {}", counter);
    TimeUnit.SECONDS.sleep(wait);
    log.info("counter after wait: {}", counter);
    log.info("end request add={}, wait={}", add, wait);

    session.setAttribute("counter", counter);

    var result = new ExampleResponse();
    result.setCounter(counter);
    result.setHost(System.getenv("HOSTNAME"));
    return result;
  }

  @GET
  @Path("/current")
  public ExampleResponse getCurrent(@Context HttpServletRequest request) {
    var session = request.getSession(true);
    Integer counter = (Integer) session.getAttribute("counter");
    var result = new ExampleResponse();
    result.setCounter(counter == 0 ? 1 : counter);
    result.setHost(System.getenv("HOSTNAME"));
    return result;
  }
}
