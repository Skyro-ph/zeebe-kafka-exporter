/*
 * Copyright © 2019 camunda services GmbH (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.zeebe.exporters.kafka.util;

import io.camunda.zeebe.exporter.api.context.Controller;
import io.camunda.zeebe.exporter.api.context.ScheduledTask;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MockController implements Controller {

  public static final long UNKNOWN_POSITION = -1;

  private final List<MockScheduledTask> scheduledTasks = new ArrayList<>();
  private long lastRanAtMs = 0;
  private long position = UNKNOWN_POSITION;

  @Override
  public void updateLastExportedRecordPosition(final long position) {
    this.position = position;
  }

  @Override
  public ScheduledTask scheduleCancellableTask(final Duration delay, final Runnable task) {
    final var scheduledTask = new MockScheduledTask(delay, task);
    scheduledTasks.add(scheduledTask);
    return scheduledTask::cancel;
  }

  public void resetScheduler() {
    lastRanAtMs = 0;
    scheduledTasks.clear();
  }

  public List<MockScheduledTask> getScheduledTasks() {
    return scheduledTasks;
  }

  public long getPosition() {
    return position;
  }

  /**
   * Will run all tasks scheduled since the last time this was executed + the given duration.
   *
   * @param elapsed upper bound of tasks delay
   */
  public void runScheduledTasks(final Duration elapsed) {
    final Duration upperBound = elapsed.plusMillis(lastRanAtMs);

    scheduledTasks.stream()
      .filter(t -> t.getDelay().compareTo(upperBound) <= 0)
      .sorted(Comparator.comparing(MockScheduledTask::getDelay))
      .forEach(MockScheduledTask::run);

    lastRanAtMs = upperBound.toMillis();
  }

  @Override
  public void updateLastExportedRecordPosition(long position, byte[] metadata) {
    this.position = position;
  }

  @Override
  public long getLastExportedRecordPosition() {
    return position;
  }

  @Override
  public Optional<byte[]> readMetadata() {
    return Optional.empty();
  }
}
