package io.github.ilyazinkovich.scientist;

import static io.github.ilyazinkovich.scientist.ExperimentResult.CANDIDATE_FAILED;
import static io.github.ilyazinkovich.scientist.ExperimentResult.RESULTS_DO_NOT_MATCH;
import static io.github.ilyazinkovich.scientist.ExperimentResult.RESULTS_MATCH;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class Science {

  private final ScheduledExecutorService scheduledExecutorService;
  private final Long reportingPeriod;
  private final TimeUnit timeUnit;

  public Science(final ScheduledExecutorService scheduledExecutorService,
      final Long reportingPeriod, final TimeUnit timeUnit) {
    this.scheduledExecutorService = scheduledExecutorService;
    this.reportingPeriod = reportingPeriod;
    this.timeUnit = timeUnit;
  }

  public Experiment experiment(final String experimentName) {
    final Map<ExperimentResult, LongAdder> results = createEmptyExperimentResultsMap();
    final ExperimentResultsAggregator aggregator = new ExperimentResultsAggregator(results);
    final ExperimentResultsLogger logger = new ExperimentResultsLogger(experimentName);
    scheduledExecutorService.scheduleAtFixedRate(() -> logger.log(aggregator.summary()),
        reportingPeriod, reportingPeriod, timeUnit);
    return new Experiment(aggregator);
  }

  private Map<ExperimentResult, LongAdder> createEmptyExperimentResultsMap() {
    final Map<ExperimentResult, LongAdder> outcomes = new EnumMap<>(ExperimentResult.class);
    outcomes.put(RESULTS_MATCH, new LongAdder());
    outcomes.put(RESULTS_DO_NOT_MATCH, new LongAdder());
    outcomes.put(CANDIDATE_FAILED, new LongAdder());
    return outcomes;
  }
}
