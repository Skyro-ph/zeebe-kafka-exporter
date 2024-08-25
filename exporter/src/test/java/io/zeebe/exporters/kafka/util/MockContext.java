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

import io.camunda.zeebe.exporter.api.context.Configuration;
import io.camunda.zeebe.exporter.api.context.Context;
import org.slf4j.Logger;

public class MockContext implements Context {
  private final Logger logger;
  private final Configuration configuration;
  private Context.RecordFilter filter;

  public MockContext(final Logger logger, final Configuration configuration) {
    this.logger = logger;
    this.configuration = configuration;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Configuration getConfiguration() {
    return configuration;
  }

  public Context.RecordFilter getFilter() {
    return filter;
  }

  @Override
  public void setFilter(final RecordFilter filter) {
    this.filter = filter;
  }

  @Override
  public int getPartitionId() {
    return 0;
  }
}
