/*
 * Copyright 2022 The Blaze Authors
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
package org.apache.spark.sql.execution.blaze.plan

import org.apache.spark.sql.execution.SparkPlan
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.catalog.CatalogTable
import org.apache.spark.sql.execution.metric.SQLMetric

import com.thoughtworks.enableIf

case class NativeParquetSinkExec(
    sparkSession: SparkSession,
    table: CatalogTable,
    partition: Map[String, Option[String]],
    override val child: SparkPlan,
    override val metrics: Map[String, SQLMetric])
    extends NativeParquetSinkBase(sparkSession, table, partition, child, metrics) {

  @enableIf(
    Seq("spark320", "spark324", "spark333", "spark351").contains(
      System.getProperty("blaze.shim")))
  override protected def withNewChildInternal(newChild: SparkPlan): SparkPlan =
    copy(child = newChild)

  @enableIf(Seq("spark303").contains(System.getProperty("blaze.shim")))
  override def withNewChildren(newChildren: Seq[SparkPlan]): SparkPlan =
    copy(child = newChildren.head)
}
