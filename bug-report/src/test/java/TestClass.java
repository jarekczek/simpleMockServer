import org.junit.Assert;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class TestClass {
  Logger log;

  public TestClass() {
    log = LoggerFactory.getLogger(this.getClass());
  }

  @Test
  public void main() throws IOException {
    Assert.assertNotNull("socket read timeout must be set", System.getProperty("sun.net.client.defaultReadTimeout"));
    boolean finishedWithoutException = false;
    ClientAndServer cliSer = new ClientAndServer(1090);
    try {
      cliSer.when(request().withPath("/atonce"))
        .respond(response().withBody("here you are").withHeader("Content-type", "text/plain"));
      cliSer.when(request().withPath("/back"))
        .callback(new ExpectationCallback() {
          @Override
          public HttpResponse handle(HttpRequest httpRequest) {
            return response().withBody("callback is active").withHeader("Content-type", "text/plain");
          }
        });

      call(2, 0);
      finishedWithoutException = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      cliSer.stop();
    }
    assert finishedWithoutException;
  }

  private void call(int count, int delay) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(count);
    AtomicInteger okCount = new AtomicInteger(0);
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          log.info("new thread is started " + Thread.currentThread());
          URL url = new URL("http://localhost:1090/back");
          URLConnection con = url.openConnection();
          boolean canRead = con.getInputStream().read() >= 0;
          System.out.println("can read: " + canRead);
          Assert.assertTrue(canRead);
          okCount.incrementAndGet();
        } catch(Exception e) {
          throw new RuntimeException(e);
        } finally {
          latch.countDown();
          log.info("thread " + Thread.currentThread() + " finished");
        }
      }
    };
    IntStream.range(0, count).forEach( (int i) -> {
        Thread th = new Thread(runnable);
        th.start();
        try {
          Thread.sleep(delay);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    );
    log.info("waiting for main latch");
    latch.await();
    log.info("latch released");
    Assert.assertEquals("okCount", count, okCount.get());
  }
}