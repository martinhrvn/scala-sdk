// Copyright (C) 2016 IBM Corp. All Rights Reserved.
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
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.{Classifier,Classifiers}
import com.ibm.watson.developer_cloud.service.{LocalFileConfigFactory, ConfigFactory}
import org.junit.runner.RunWith
import org.scalatest.time.{Seconds, Span, Millis}
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit._

/**
  * Created by martinhrvn on 07/04/16.
  */
class NaturalLanguageClassifierIT extends FlatSpec with ScalaFutures {
  val config : ConfigFactory = LocalFileConfigFactory("/vcap_services.json")

  implicit val system = ActorSystem()
  implicit val defaultPatience =
    PatienceConfig(timeout = Span(15, Seconds), interval = Span(500, Millis))

  "NaturalLanguageClassifier.getClassifiers" should "retrieve classifiers" in {
    val service = new NaturalLanguageClassifier(config)

    val futureClassifier = service.getClassifiers
    val futureValue = futureClassifier.futureValue

     assert(!futureValue.classifiers.isEmpty)

  }

}
