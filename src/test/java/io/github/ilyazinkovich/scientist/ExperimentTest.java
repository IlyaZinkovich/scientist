package io.github.ilyazinkovich.scientist;

import static io.github.ilyazinkovich.scientist.Outcome.CANDIDATE_FAILED;
import static io.github.ilyazinkovich.scientist.Outcome.RESULTS_DO_NOT_MATCH;
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

    final Supplier<Integer> function = () -> 1;
    final Integer result = experiment.run(function, function);

    assertEquals(function.get(), result);
    await().atMost(ONE_SECOND).untilTrue(booleanResult);
  }

  @Test
  void testRunComparingDifferentFunctionResults() {
    final AtomicBoolean booleanResult = new AtomicBoolean();
    final Experiment experiment =
        new Experiment(result -> booleanResult.set(result == RESULTS_DO_NOT_MATCH));

    final Supplier<Integer> controlFunction = () -> 1;
    final Supplier<Integer> candidateFunction = () -> 2;
    final Integer result = experiment.run(controlFunction, candidateFunction);

    assertEquals(controlFunction.get(), result);
    await().atMost(ONE_SECOND).untilTrue(booleanResult);
  }

  @Test
  void testRunWithCandidateFunctionThrowingException() {
    final AtomicBoolean booleanResult = new AtomicBoolean();
    final Experiment experiment =
        new Experiment(result -> booleanResult.set(result == CANDIDATE_FAILED));

    final Supplier<Integer> controlFunction = () -> 1;
    final Supplier<Integer> candidateFunction = () -> {
      throw new RuntimeException();
    };
    final Integer result = experiment.run(controlFunction, candidateFunction);

    assertEquals(controlFunction.get(), result);
    await().atMost(ONE_SECOND).untilTrue(booleanResult);
  }
}
