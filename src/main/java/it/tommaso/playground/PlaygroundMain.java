package it.tommaso.playground;

import it.tommaso.playground.bingo.RandomNumberExtractor;
import it.tommaso.playground.bingo.StripGenerator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class PlaygroundMain {

  public static void main(final String[] args) throws InterruptedException {

    long startTime = System.currentTimeMillis();

    ExecutorService executorService = Executors.newWorkStealingPool(Runtime.getRuntime()
        .availableProcessors() * (1 + 1 / 250));

    IntStream.range(0, 10000).forEach(index -> executorService.execute(() -> new StripGenerator(new
        RandomNumberExtractor()).printStrips()));

    executorService.shutdown();
    executorService.awaitTermination(10, TimeUnit.SECONDS);

    long endTime = System.currentTimeMillis();

    System.out.println("It took " + (endTime - startTime)
        + " milliseconds to generate 10K Strips file in logs folder");
  }

}
