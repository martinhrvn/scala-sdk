package com.ibm.watson.developercloud.natural_language_classifier.v1

import com.ibm.watson.developercloud.natural_language_classifier.v1.NaturalLanguageClassifierProtocol._
import com.ibm.watson.developercloud.utils.{Validation, WatsonServiceConfig, WatsonService}
import com.typesafe.scalalogging.LazyLogging
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json._

import scala.concurrent.Future



class NaturalLanguageClassifier(config: WatsonServiceConfig) extends WatsonService(config) with LazyLogging {
  import system.dispatcher
  def serviceType = "natural_language_classifier"
  val CLASSIFIERS_URL = "/v1/classifiers"
  val CLASSIFIER_URL = "/v1/classifiers/%s"
  val CLASSIFICATION_URL = "/v1/classifiers/%s/classify"


  def createClassifier(language: String, trainingData: List[TrainingData]): Future[Classifier] = {
    var map = Map[String, JsValue]()
    Validation.notEmpty(language, "Language cannot be empty")
    //TODO: Validation.notEmpty(trainingData, "Training data cannot be empty")
    map + ("language" -> JsString(language))

    map = Option(trainingData) match {
      case Some(testData) if testData.nonEmpty => map + ("training_data" -> trainingData.toJson)
      case _ => throw new IllegalArgumentException("Test data cannot be empty")
    }

    val jsonRequest = new JsObject(map)

    val post: HttpRequest = Post(endpoint + CLASSIFIERS_URL, jsonRequest.toString())
    send(post).map(unmarshal[Classifier])
  }

  def classify(classifierId : String, text: String) : Future[Classification] = {
    Validation.notEmpty(classifierId, "Classifier ID cannot be empty")
    Validation.notEmpty(text, "Text cannot be empty")

    val url = CLASSIFICATION_URL.format(classifierId)
    val jsonRequest = JsObject("text" -> JsString(text))

    val request: HttpRequest = Post(endpoint + url, jsonRequest.toString())
    send(request).map(unmarshal[Classification])
  }

  def getClassifiers : Future[List[Classifier]] = {
    logger.info("Running getClassifiers method")
    val response: Future[HttpResponse] = send(Get(endpoint + CLASSIFIERS_URL))
    logger.info("returning a response")
    response.map(unmarshal[List[Classifier]])
  }

  def deleteClassifier(classifierId : String) : Future[HttpResponse] = {
    Validation.notEmpty(classifierId, "Classifier ID cannot be empty")
    val url = CLASSIFIER_URL.format(classifierId)

    val response : Future[HttpResponse] = send(Delete(endpoint + url))
    response
  }

  def getClassifier(classifierId : String) : Future[Classifier] = {
    Validation.notEmpty(classifierId, "Classifier ID cannot be empty")

    val url = CLASSIFIER_URL.format(classifierId)

    val response : Future[HttpResponse] = send(Get(endpoint + url))

    response.map(unmarshal[Classifier])
  }
}
