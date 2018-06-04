package io.github.ilyazinkovich.scientist;

import static java.lang.Boolean.FALSE;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.ONE_SECOND;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExperimentTest {

  @Test
  void testRun() {
    final Experiment experiment = new Experiment(Assertions::assertTrue);
    experiment.run(() -> 1 + 2, () -> 2 + 1);
  }

  @Test
  void testRunAsync() {
    final AtomicBoolean resultHolder = new AtomicBoolean(FALSE);
    final Experiment experiment = new Experiment(resultHolder::set);
    experiment.runAsync(() -> 1 + 2, () -> 2 + 1);
    await().atMost(ONE_SECOND).untilTrue(resultHolder);
  }
}
