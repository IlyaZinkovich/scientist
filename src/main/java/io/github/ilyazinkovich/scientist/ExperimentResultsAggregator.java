package io.github.ilyazinkovich.scientist;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

public class ExperimentResultsAggregator implements Consumer<ExperimentResult> {

  private final Map<ExperimentResult, LongAdder> results;

  public ExperimentResultsAggregator(final Map<ExperimentResult, LongAdder> results) {
    this.results = results;
  }

  @Override
  public void accept(final ExperimentResult outcome) {
    final LongAdder accumulator = results.get(outcome);
    accumulator.increment();
  }

  public Map<ExperimentResult, Long> summary() {
    return results.entrySet().stream()
        .collect(toMap(Entry::getKey, entry -> entry.getValue().longValue()));
  }
}
