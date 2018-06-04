package io.github.ilyazinkovich.scientist;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.Map;

public class ExperimentResultsLogger {

  private final String experimentName;

  ExperimentResultsLogger(final String experimentName) {
    this.experimentName = experimentName;
  }

  void log(final Map<ExperimentResult, Long> results) {
    final String resultsString = results.entrySet().stream()
        .map(entry -> format("%s: %d", entry.getKey().name(), entry.getValue()))
        .collect(joining(", "));
    System.out.printf("%s results: %s%n", experimentName, resultsString);
  }
}
