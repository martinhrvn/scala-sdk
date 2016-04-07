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

import java.util
import java.util.Date

import akka.actor.ActorSystem
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.NaturalLanguageClassifierProtocol._
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.{Classifier, ClassifiedClass, Classification}
import com.ibm.watson.developer_cloud.service.ManualConfig
import org.joda.time.DateTime
import org.scalatest.junit._
import com.typesafe.scalalogging._
import org.junit._
import scala.util.{Success, Failure}
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.concurrent._
import scala.concurrent.duration._
import spray.json._

/**
 * @author Martin Harvan (martin.harvan@sk.ibm.com)
 */

class NaturalLanguageClassifierTest extends FlatSpec  {
  implicit val system = ActorSystem()
  "Service type for NaturalLanguageClassifier" should "be natural_language_classifier" in {

    val a = new NaturalLanguageClassifier(new ManualConfig("test", "test", "test"))
    assert(a.serviceType.equals("natural_language_classifier"))
  }

  "Classification" should "be parsed correctly" in {
    val json = scala.io.Source.fromURL(getClass.getResource("/json/natural_language_classifier/classification.json")).mkString
    val classification = json.parseJson.convertTo[Classification]

    assert(classification.id.equals("1234"))
    assert(classification.url.equals("https://sample.com/url"))
    assert(classification.text.equals("test text"))
    assert(classification.topClass.equals("temperature"))
  }

  it should "be outputted correctly" in {
    val classification = Classification("id", "http://example.com/test", "test text", "topClass", List.empty[ClassifiedClass])
    val generatedJson = classification.toJson.compactPrint

    val reReadJson = generatedJson.parseJson.convertTo[Classification]
    assert(classification.equals(reReadJson))
  }

  "ClassifiedClass" should "be parsed correctly" in {
    val json = scala.io.Source.fromURL(getClass.getResource("/json/natural_language_classifier/classified_class.json")).mkString
    val classification = json.parseJson.convertTo[ClassifiedClass]

    assert(classification.name.equals("temperature"))
    assert(classification.confidence.equals(0.33))
  }

  it should "be outputed correctly" in {
    val classifiedClass = ClassifiedClass("name", 0.22)
    val json = classifiedClass.toJson.compactPrint
    val parsed = json.parseJson.convertTo[ClassifiedClass]
    assert(parsed.equals(classifiedClass))
  }

  "ClassifierClass" should "be parsed correctly" in {
    val json = scala.io.Source.fromURL(getClass.getResource("/json/natural_language_classifier/classifier.json")).mkString
    val classification = json.parseJson.convertTo[Classifier]

    assert(classification.name.equals("Music controls"))
    assert(classification.id.equals("5E00F7x2-nlc-507"))
    assert(classification.url.equals("https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/5E00F7x2-nlc-507"))
    assert(classification.language.equals("en"))
    assert(classification.created.equals(new DateTime("2015-10-17T20:56:29.974Z")))
  }
}
