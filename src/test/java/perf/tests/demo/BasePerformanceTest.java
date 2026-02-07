package perf.tests.demo;

import org.apache.jmeter.JMeter;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.jupiter.api.AfterAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class BasePerformanceTest {

  protected static final String REPORT_DIR = "target/jmeter-report";
  protected static final String JTL_DIR = "target/jtl-results";
  protected static final String JTL_FILE = "report.jtl";

  @AfterAll
  public static void generateReports() throws Exception {
    File reportDir = new File(REPORT_DIR);
    File jtlFile = new File(new File(JTL_DIR), JTL_FILE);

    // Clean report directory to avoid "folder is not empty" error
    if (reportDir.exists()) {
      deleteDirectory(reportDir.toPath());
    }
    reportDir.mkdirs();

    if (jtlFile.exists()) {
      // 1. Generate dynamic dashboard
      JMeterUtils.setProperty(JMeter.JMETER_REPORT_OUTPUT_DIR_PROPERTY, reportDir.getAbsolutePath());
      new ReportGenerator(jtlFile.getPath(), null).generate();

      // 2. Generate single static HTML report
      HtmlReportGenerator.generate(reportDir);
    } else {
      System.out.println("Report generation skipped: JTL file not found at " + jtlFile.getAbsolutePath());
    }
  }

  private static void deleteDirectory(Path path) throws IOException {
    try (Stream<Path> walk = Files.walk(path)) {
      walk.sorted(Comparator.reverseOrder())
          .map(Path::toFile)
          .forEach(file -> {
            if (!file.delete()) {
              System.err.println("Failed to delete file: " + file.getAbsolutePath());
            }
          });
    }
  }
}
