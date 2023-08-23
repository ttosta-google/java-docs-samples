/*
 * Copyright 2023 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.spanner.jdbc;

//[START spanner_jdbc_auto_partition_mode]
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AutoPartitionModeExample {

  public static void main(String[] args) throws SQLException {
    autoPartitionMode();
  }

  static void autoPartitionMode() throws SQLException {
    // TODO(developer): Replace these variables before running the sample.
    String projectId = "my-project";
    String instanceId = "my-instance";
    String databaseId = "my-database";
    autoPartitionMode(projectId, instanceId, databaseId);
  }

  // This example shows how to use 'auto_partition_mode=true' to execute partitioned queries with
  // the JDBC driver.
  static void autoPartitionMode(String projectId, String instanceId, String databaseId)
      throws SQLException {
    String connectionUrl = String.format("jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s",
        projectId, instanceId, databaseId);
    try (Connection connection = DriverManager.getConnection(
        connectionUrl); Statement statement = connection.createStatement()) {
      // A connection can also be set to 'auto_partition_mode', which will instruct it to execute
      // all queries as a partitioned query. This is essentially the same as automatically prefixing
      // all queries with 'RUN PARTITIONED QUERY ...'.
      statement.execute("set auto_partition_mode=true");
      // This will execute at most max_partitioned_parallelism partitions in parallel.
      statement.execute("set max_partitioned_parallelism=8");
      try (ResultSet resultSet = statement.executeQuery(
          "SELECT SingerId, FirstName, LastName FROM singers")) {
        while (resultSet.next()) {
          System.out.printf("%s %s %s%n", resultSet.getString(1), resultSet.getString(2),
              resultSet.getString(3));
        }
      }
    }
  }
}
//[END spanner_jdbc_auto_partition_mode]
