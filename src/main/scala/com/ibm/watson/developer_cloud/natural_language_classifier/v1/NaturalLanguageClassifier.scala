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
package com.ibm.watson.developer_cloud.natural_language_classifier.v1

import com.ibm.watson.developer_cloud.utils.{JsonUtils, Validation, WatsonServiceConfig, WatsonService}
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json._
import NaturalLanguageClassifierProtocol._
import NaturalLanguageClassifier._
import scala.concurrent.Future


/**
  * The IBM Watson Natural Language Classifier service applies deep learning techniques to make
  * predictions about the best predefined classes for short sentences or phrases. The classes can
  * trigger a corresponding action in an application, such as directing a request to a location or
  * person, or answering a question. After training, the service returns information for texts that
  * it hasn't seen before. The response includes the name of the top classes and confidence values.
  * @param config Configuration for service
  */
class NaturalLanguageClassifier(config: WatsonServiceConfig) extends WatsonService(config) {
  val logger = LoggerFactory.getLogger(classOf[NaturalLanguageClassifier])
  def serviceType : String = "natural_language_classifier"

  /**
    * Sends data to create and train a classifier, and returns information about the new classifier.
    * The status has the value of `Training` when the operation is successful, and might remain at
    * this status for a while.
    *
    * @param name name of the classifier
    * @param language IETF primary language for the classifier. for example: 'en'
    * @param trainingData The set of questions and their "keys" used to adapt a system to a domain
    *        (the ground truth)
    * @return created classifier
    */
  def createClassifier(name: String, language: String, trainingData: List[TrainingData]): Future[Classifier] = {
    Validation.notEmpty(language, "Language cannot be empty")

    val map = Option(language).map("language" -> JsString(_)).toMap ++
    Option(name).map("name" -> JsString(_)).toMap ++
    Option(trainingData).map("training_data" -> _.toJson).toMap
    //TODO: Validation.notEmpty(trainingData, "Training data cannot be empty")

    val jsonRequest = new JsObject(map)

    val post: HttpRequest = Post(config.endpoint + classifiersPath, jsonRequest.toString())
    send(post).map(unmarshal[Classifier])
  }

  /**
    * Returns classification information for a classifier on a phrase.
    *
    * @param classifierId the classifier id
    * @param text the submitted phrase to classify
    * @return the classification of the phrase with a given classifier
    */
  def classify(classifierId : String, text: String) : Future[Classification] = {
    Validation.notEmpty(classifierId, "Classifier ID cannot be empty")
    Validation.notEmpty(text, "Text cannot be empty")

    val url = classificationPath.format(classifierId)
    val jsonRequest = JsObject("text" -> JsString(text))

    val request: HttpRequest = Post(config.endpoint + url, jsonRequest.toString())
    send(request).map(unmarshal[Classification])
  }

  /**
    * Retrieves the list of classifiers for the user.
    *
    * @return the classifier list
    */
  def getClassifiers : Future[List[Classifier]] = {
    logger.info("Running getClassifiers method")
    val response: Future[HttpResponse] = send(Get(config.endpoint + classifiersPath))
    logger.info("returning a response")
    response.map(unmarshal[List[Classifier]])
  }

  /**
    * Deletes a classifier
    *
    * @param classifierId the classifier ID
    * @return future of HttpResponse
    */
  def deleteClassifier(classifierId : String) : Future[HttpResponse] = {
    Validation.notEmpty(classifierId, "Classifier ID cannot be empty")
    val url = classifierPathL.format(classifierId)

    val response : Future[HttpResponse] = send(Delete(config.endpoint + url))
    response
  }

  /**
    * Retrieves a classifier by ID
    * @param classifierId ID of the classifier
    * @return Classifier
    */
  def getClassifier(classifierId : String) : Future[Classifier] = {
    Validation.notEmpty(classifierId, "Classifier ID cannot be empty")

    val url = classifierPathL.format(classifierId)

    val response : Future[HttpResponse] = send(Get(config.endpoint + url))

    response.map(unmarshal[Classifier])
  }
}

object NaturalLanguageClassifier {
  val classifiersPath = "/v1/classifiers"
  val classifierPathL = "/v1/classifiers/%s"
  val classificationPath = "/v1/classifiers/%s/classify"

}