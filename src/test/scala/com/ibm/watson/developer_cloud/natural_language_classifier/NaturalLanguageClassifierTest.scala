// Copyright (C) 2015 IBM Corp. All Rights Reserved.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.ibm.watson.developer_cloud.natural_language_classifier

import akka.actor.ActorSystem
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.{NaturalLanguageClassifier, TrainingData}
import com.ibm.watson.developer_cloud.utils.{ManualConfig, ManualServicesConfig, VCAPServicesConfig}
import org.scalatest.junit._
import com.typesafe.scalalogging._
import org.junit._
import scala.util.{Success, Failure}
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.concurrent._
import scala.concurrent.duration._

/**
 * @author Martin Harvan (martin.harvan@sk.ibm.com)
 */
@RunWith(classOf[JUnitRunner])
class NaturalLanguageClassifierTest extends FlatSpec with LazyLogging  {
  implicit val system = ActorSystem()
  "Service type for NaturalLanguageClassifier" should "be natural_language_classifier" in {

    val a = new NaturalLanguageClassifier(new ManualConfig("test", "test", "test"))
    assert(a.serviceType.equals("natural_language_classifier"))
    //assert(stack.pop() === 1)
  }
}
