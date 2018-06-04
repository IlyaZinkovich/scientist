package io.github.ilyazinkovich.scientist;

import static io.github.ilyazinkovich.scientist.Outcome.CANDIDATE_FAILED;
import static io.github.ilyazinkovich.scientist.Outcome.RESULTS_MATCH;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.ONE_MINUTE;
import static org.awaitility.Duration.ONE_SECOND;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ExperimentTest {

  @Test
  void testRun() {
    final AtomicBoolean booleanResult = new AtomicBoolean();
    final Experiment experiment =
        new Experiment(result -> booleanResult.set(result == RESULTS_MATCH));

    experiment.run(() -> 1 + 2, () -> 2 + 1);

    await().atMost(ONE_SECOND).untilTrue(booleanResult);
  }

  @Test
  void testRunCandidateException() {
    final AtomicBoolean booleanResult = new AtomicBoolean();
    final Experiment experiment =
        new Experiment(result -> booleanResult.set(result == CANDIDATE_FAILED));

    experiment.run(() -> 1 + 2, (Supplier<Object>) () -> {
      throw new RuntimeException();
    });

    await().atMost(ONE_MINUTE).untilTrue(booleanResult);
  }
}
