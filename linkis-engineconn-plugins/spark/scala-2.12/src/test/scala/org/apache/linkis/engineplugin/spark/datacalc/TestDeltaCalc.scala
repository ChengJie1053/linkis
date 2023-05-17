/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.linkis.engineplugin.spark.datacalc

import org.apache.linkis.engineplugin.spark.datacalc.model.DataCalcGroupData

import org.junit.jupiter.api.{Assertions, Test}

class TestDelta {

  @Test
  def testDataLakeWrite: Unit = {
    val csvFilePath = this.getClass.getResource("/etltest.dolphin").getFile
    val data = DataCalcGroupData.getData(writeConfigJson.replace("{csvFilePath}", csvFilePath))
    Assertions.assertTrue(data != null)

    val (sources, transforms, sinks) = DataCalcExecution.getPlugins(data)

    Assertions.assertTrue(sources != null)
    Assertions.assertTrue(transforms != null)
    Assertions.assertTrue(sinks != null)
  }

  @Test
  def testDataLakeReader: Unit = {
    val data = DataCalcGroupData.getData(readerConfigJson)
    Assertions.assertTrue(data != null)

    val (sources, transforms, sinks) = DataCalcExecution.getPlugins(data)

    Assertions.assertTrue(sources != null)
    Assertions.assertTrue(transforms != null)
    Assertions.assertTrue(sinks != null)
  }

  val writeConfigJson =
    """
      |{
      |    "sources": [
      |        {
      |            "name": "file",
      |            "type": "source",
      |            "config": {
      |                "resultTable": "T1654611700631",
      |                "path": "file://{csvFilePath}",
      |                "serializer": "csv",
      |                "options": {
      |                "header":"true",
      |                "delimiter":";"
      |                },
      |                "columnNames": ["name", "age"]
      |            }
      |        }
      |    ],
      |    "transformations": [
      |        {
      |            "name": "sql",
      |            "type": "transformation",
      |            "config": {
      |                "resultTable": "T111",
      |                "sql": "select * from T1654611700631"
      |            }
      |        }
      |    ],
      |    "sinks": [
      |        {
      |            "name": "datalake",
      |            "config": {
      |                "sourceTable": "T1654611700631",
      |                "tableFormat": "delta",
      |                "path": "file:///Users/chengjie/cjtest/test",
      |                "saveMode": "overwrite"
      |            }
      |        }
      |    ]
      |}
      |""".stripMargin

  val readerConfigJson =
    """
      |{
      |    "sources": [
      |        {
      |            "name": "datalake",
      |            "type": "source",
      |            "config": {
      |                "resultTable": "T1654611700631",
      |                "tableFormat": "delta",
      |                "path": "file:///Users/chengjie/cjtest/test"
      |            }
      |        }
      |    ],
      |    "transformations": [
      |        {
      |            "name": "sql",
      |            "type": "transformation",
      |            "config": {
      |                "resultTable": "T111",
      |                "sql": "select * from T1654611700631"
      |            }
      |        }
      |    ],
      |    "sinks": [
      |        {
      |            "name": "file",
      |            "config": {
      |                "sourceTable": "T1654611700631",
      |                "path": "file:///Users/chengjie/cjtest/csv",
      |                "saveMode": "overwrite",
      |                "options": {
      |                "header":"true"
      |                },
      |                "serializer": "csv"
      |            }
      |        }
      |    ]
      |}
      |""".stripMargin

}
