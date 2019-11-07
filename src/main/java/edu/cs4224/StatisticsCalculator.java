package edu.cs4224;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper function to calculate overall statistics.
 */
public class StatisticsCalculator {
  private static final String REPORT_DELIMITER = "====";

  /**
   * Log formats.
   */
  private static final String TotalNumberOfTransaction = "Total number of transactions processed: (\\d+)";
  private static final String TotalElapsedTime = "Total elapsed time: (\\d+)s";
  private static final String TransactionThroughput = "Transaction throughput: (\\d+) per second";
  private static final String AverageTransactionLatency = "Average transaction latency: (\\d+)ms";
  private static final String MedianTransactionLatency = "Median transaction latency: (\\d+)ms";
  private static final String NinetyFivePercentileTransactionLatency = "95th percentile transaction latency: (\\d+)ms";
  private static final String NinetyNinePercentileTransactionLatency = "99th percentile transaction latency: (\\d+)ms";

  public void run(String logPath, int NC) throws Exception {
    int totalNumberOfTransaction = 0;
    int totalExecutionTime = -1;
    int transactionThroughput = 0;
    int percentile95 = 0;
    int minThroughput = Integer.MAX_VALUE;
    int maxThroughput = Integer.MIN_VALUE;

    for (int i = 1; i <= NC; i++) {
      String log = fetchLog(logPath, i);

      totalNumberOfTransaction += regex(log, TotalNumberOfTransaction);
      totalExecutionTime = Math.max(totalExecutionTime, regex(log, TotalElapsedTime));
      transactionThroughput += regex(log, TransactionThroughput);
      percentile95 += regex(log, NinetyFivePercentileTransactionLatency);

      minThroughput = Math.min(minThroughput, regex(log, TransactionThroughput));
      maxThroughput = Math.max(maxThroughput, regex(log, TransactionThroughput));
    }

    System.out.println("total number of transaction: " + totalNumberOfTransaction);
    System.out.println("total execution time: " + totalExecutionTime);
    System.out.println("transaction throughput: " + (transactionThroughput * 1.0 / NC));
    System.out.println("min transaction Throughput: " + minThroughput);
    System.out.println("max transaction Throughput: " + maxThroughput);
    System.out.println("95th percentile latency: " + (percentile95 * 1.0 / NC));
  }

  public void stripStatisticLog(String logFolderPath) throws Exception {
    File file = new File(logFolderPath);

    // Checks whether it is a folder.
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      if (files == null) {
        System.out.println("Skipped because there is no file inside " + logFolderPath);
        return;
      }

      // Recursively processes all files & sub-folders.
      for (File subFolder : files) {
        stripStatisticLog(subFolder.getAbsolutePath());
      }
      return;
    } else if (!logFolderPath.endsWith(".err.log")) {
      System.out.println("Skipped because the file name is incorrect: " + logFolderPath);
      return;
    }

    System.out.println("Begin to strip log from " + logFolderPath);
    String log = fetchLog(file);
    String result = log.substring(log.indexOf(REPORT_DELIMITER),
        log.lastIndexOf(REPORT_DELIMITER) + REPORT_DELIMITER.length());

    try (FileWriter writer = new FileWriter(file)) {
      writer.write(result);
    }
  }

  private String fetchLog(String logPath, int index) throws Exception {
    return fetchLog(new File(String.format("%s/%d.err.log", logPath, index)));
  }

  private String fetchLog(File file) throws Exception {
    try (
        BufferedReader reader = new BufferedReader(new FileReader(file));
    ) {
      final StringBuilder builder = new StringBuilder();
      while (reader.ready()) {
        builder.append(reader.readLine());
        builder.append("\n");
      }
      return builder.toString();
    }
  }

  private int regex(String content, String patternStr) {
    Pattern pattern = Pattern.compile(patternStr);
    Matcher match = pattern.matcher(content);
    match.find();
    return Integer.parseInt(match.group(1));
  }
}