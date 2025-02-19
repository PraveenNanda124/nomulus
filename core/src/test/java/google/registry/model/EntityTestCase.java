// Copyright 2017 The Nomulus Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package google.registry.model;

import static org.joda.time.DateTimeZone.UTC;

import google.registry.testing.AppEngineExtension;
import google.registry.testing.FakeClock;
import org.joda.time.DateTime;
import org.junit.jupiter.api.extension.RegisterExtension;

/** Base class of all unit tests for entities which are persisted to Datastore via Objectify. */
public abstract class EntityTestCase {

  protected enum JpaEntityCoverageCheck {
    /**
     * The test will contribute to the coverage checks in {@link
     * google.registry.schema.integration.SqlIntegrationTestSuite}.
     */
    ENABLED,
    /** The test is not relevant for JPA coverage checks. */
    DISABLED
  }

  protected FakeClock fakeClock = new FakeClock(DateTime.now(UTC));

  @RegisterExtension public final AppEngineExtension appEngine;

  protected EntityTestCase() {
    this(JpaEntityCoverageCheck.DISABLED);
  }

  protected EntityTestCase(JpaEntityCoverageCheck jpaEntityCoverageCheck) {
    appEngine =
        AppEngineExtension.builder()
            .withCloudSql()
            .enableJpaEntityCoverageCheck(jpaEntityCoverageCheck == JpaEntityCoverageCheck.ENABLED)
            .withClock(fakeClock)
            .build();
  }
}
