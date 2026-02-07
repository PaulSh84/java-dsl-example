package perf.tests.demo;

import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;

import java.io.IOException;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;


public class PerformanceTest extends BasePerformanceTest {

  @Test
  public void testOpenPage() throws IOException {
    TestPlanStats stats = testPlan(
        threadGroup("Open Page", 1, 1,
            httpSampler("Open Home Page", "https://jsonplaceholder.typicode.com/")
        ),
        jtlWriter(JTL_DIR, JTL_FILE)
    ).run();
  }

  @Test
  public void testListUsers() throws IOException {
    TestPlanStats stats = testPlan(
        threadGroup("List Users", 5, 10,
            httpSampler("List Users", "https://jsonplaceholder.typicode.com/users")
                .method("GET")
        ),
        jtlWriter(JTL_DIR, JTL_FILE)
    ).run();
  }

  @Test
  public void testCreatePost() throws IOException {
    TestPlanStats stats = testPlan(
        threadGroup("Create Post", 5, 10,
            httpSampler("Create Post", "https://jsonplaceholder.typicode.com/posts")
                .method("POST")
                .body("{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}")
                .contentType(ContentType.APPLICATION_JSON)
        ),
        jtlWriter(JTL_DIR, JTL_FILE)
    ).run();
  }

  @Test
  public void testSingleUser() throws IOException {
    TestPlanStats stats = testPlan(
        threadGroup("Single User", 5, 10,
            httpSampler("Get Single User", "https://jsonplaceholder.typicode.com/users/1")
                .method("GET")
        ),
        jtlWriter(JTL_DIR, JTL_FILE)
    ).run();
  }
}
