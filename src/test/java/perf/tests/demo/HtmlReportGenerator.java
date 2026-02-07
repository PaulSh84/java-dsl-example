package perf.tests.demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlReportGenerator {

  private static final String TEMPLATE_RESOURCE = "report-template.html";

  public static void generate(File jtlFile, File outputDir) throws IOException {
    // We don't need jtl file anymore, we use the generated statistics.json
    File statsJson = new File(outputDir, "statistics.json");
    
    if (!statsJson.exists()) {
      System.out.println("statistics.json not found in " + outputDir.getAbsolutePath() + ". Cannot generate static report.");
      return;
    }

    String jsonContent = Files.readString(statsJson.toPath(), StandardCharsets.UTF_8);
    String template = readTemplate();
    
    String reportContent = template.replace("{{DASHBOARD_DATA}}", jsonContent);
    
    Path staticReportPath = new File(outputDir, "static-report.html").toPath();
    Files.writeString(staticReportPath, reportContent, StandardCharsets.UTF_8);
    System.out.println("Single static report generated at: " + staticReportPath.toAbsolutePath());
  }

  private static String readTemplate() throws IOException {
    try (InputStream is = HtmlReportGenerator.class.getClassLoader().getResourceAsStream(TEMPLATE_RESOURCE)) {
      if (is == null) {
        throw new IOException("Template resource not found: " + TEMPLATE_RESOURCE);
      }
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}
