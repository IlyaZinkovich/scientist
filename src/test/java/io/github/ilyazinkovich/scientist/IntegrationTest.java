package io.github.ilyazinkovich.scientist;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class IntegrationTest {

  private static final Random RANDOM = new Random();
  private static final long ONE = 1L;

  @Test
  void test() {
    final int singleThread = 1;
    final ScheduledExecutorService scheduledExecutorService = newScheduledThreadPool(singleThread);
    final Science science = new Science(scheduledExecutorService, ONE, MILLISECONDS);
    final String experimentName = "Experiment";
    final Experiment experiment = science.experiment(experimentName);
    final Supplier<Integer> controlFunction = () -> 1;
    final long experimentsNumber = 100L;
    Stream.generate(this::randomFunction).limit(experimentsNumber)
        .map(randomCandidateFunction -> experiment.run(controlFunction, randomCandidateFunction))
        .forEach(result -> assertEquals(controlFunction.get(), result));
  }

  private Supplier<Integer> randomFunction() {
    final int randomInt = RANDOM.nextInt(3);
    final Supplier<Integer> supplier;
    if (randomInt == 0) {
      supplier = () -> {
        throw new RuntimeException();
      };
    } else {
      supplier = () -> randomInt;
    }
    return supplier;
  }
}
