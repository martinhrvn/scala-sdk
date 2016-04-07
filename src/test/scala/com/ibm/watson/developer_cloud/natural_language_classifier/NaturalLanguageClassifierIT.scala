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

import java.io.File

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
class NaturalLanguageClassifierIT extends FlatSpec with ScalaFutures with Matchers {
  val config : ConfigFactory = LocalFileConfigFactory("/vcap_services.json")
  val service = new NaturalLanguageClassifier(config)

  implicit val system = ActorSystem()
  implicit val defaultPatience =
    PatienceConfig(timeout = Span(15, Seconds), interval = Span(500, Millis))

  "NaturalLanguageClassifier.getClassifiers" should "retrieve classifiers" in {


    val futureClassifier = service.getClassifiers
    val futureValue = futureClassifier.futureValue

     assert(futureValue.classifiers.nonEmpty)

  }

  "NaturalLanguageClassifier.createClassifier" should "create classifier for training data" in {
    val file = new File(getClass.getResource("/natural_language_classifier/weather_data_train.csv").toURI)
    val futureClassifier = service.createClassifier("itest-example", "en", file)
  }

  "NaturalLanguageClassifier.createClassifier" should "throw exception for missing training data" in {
    val file = scala.io.Source.fromURL(getClass.getResource("/natural_language_classifier/weather_data_train.csv"))
//    service.createClassifier(null, null, null) shouldBe a [ParameterMissingException]

    intercept[IllegalArgumentException] {
      val futureClassifier = service.createClassifier("itest-example", "en", null)
      val futureValue = futureClassifier.futureValue
    }

  }

}
