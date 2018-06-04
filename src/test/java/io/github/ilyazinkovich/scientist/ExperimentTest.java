package io.github.ilyazinkovich.scientist;

import static io.github.ilyazinkovich.scientist.Outcome.CANDIDATE_FAILED;
import static io.github.ilyazinkovich.scientist.Outcome.RESULTS_MATCH;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.ONE_SECOND;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ExperimentTest {

  @Test
  void testRunComparingTheSameFunctionResults() {
    final AtomicBoolean booleanResult = new AtomicBoolean();
    final Experiment experiment =
        new Experiment(result -> booleanResult.set(result == RESULTS_MATCH));

    final Supplier<Integer> function = () -> 1 + 2;
    final Integer result = experiment.run(function, function);

    assertEquals(function.get(), result);
    await().atMost(ONE_SECOND).untilTrue(booleanResult);
  }

  @Test
  void testRunWithCandidateFunctionThrowingException() {
    final AtomicBoolean booleanResult = new AtomicBoolean();
    final Experiment experiment =
        new Experiment(result -> booleanResult.set(result == CANDIDATE_FAILED));

    final Supplier<Integer> function = () -> 1 + 2;
    final Integer result = experiment.run(function, () -> {
      throw new RuntimeException();
    });

    assertEquals(function.get(), result);
    await().atMost(ONE_SECOND).untilTrue(booleanResult);
  }
}
