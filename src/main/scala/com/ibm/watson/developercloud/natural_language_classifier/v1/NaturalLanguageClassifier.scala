package com.ibm.watson.developercloud.natural_language_classifier.v1

import com.ibm.watson.developercloud.natural_language_classifier.v1.NaturalLanguageClassifierProtocol._
import com.ibm.watson.developercloud.utils.{WatsonServiceConfig, WatsonService}
import com.typesafe.scalalogging.LazyLogging
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json._

import scala.concurrent.Future



class NaturalLanguageClassifier(config: WatsonServiceConfig) extends WatsonService(config) with LazyLogging {
  import system.dispatcher
  def serviceType = "natural_language_classifier"
  val CLASSIFIER_URL = "/v1/classifiers"
  val CLASSIFICATION_URL = "/v1/classifiers/%s/classify"


  def createClassifier(language: String, trainingData: List[TrainingData]): Future[Classifier] = {
    val map = collection.mutable.Map[String, JsValue]()

    Option(language) match {
      case Some(lang) => map.put("language", JsString(lang))
      case None => throw new IllegalArgumentException("Language cannot be null")
    }

    Option(trainingData) match {
      case Some(testData) if testData.nonEmpty => map.put("training_data", trainingData.toJson)
      case _ => throw new IllegalArgumentException("Test data cannot be empty")
    }

    val jsonRequest = new JsObject(map.toMap)

    val response: Future[HttpResponse] = send(Post(endpoint + CLASSIFIER_URL, jsonRequest.toString()))

    response.map(unmarshal[Classifier])
  }

  def classify(classifierId : String, text: String) : Future[Classification] = {
    val url = Option(classifierId) match {
      case Some(cid) if classifierId.nonEmpty => CLASSIFICATION_URL.format(cid)
      case _ => throw new IllegalArgumentException("Classifier ID cannot be empty")
    }

    val jsonRequest = Option(text) match {
      case Some(t) if t.nonEmpty => JsObject(("text", JsString(t)))
      case _ => throw new IllegalArgumentException("Text cannot be empty")
    }

    val request: HttpRequest = Post(endpoint + url, jsonRequest.toString())
    send(request).map(unmarshal[Classification])
  }

  def getClassifiers : Future[List[Classifier]] = {
    logger.info("Running getClassifiers method")
    val response: Future[HttpResponse] = send(Get(endpoint + CLASSIFIER_URL))
    logger.info("returning a response")
    response.map(unmarshal[List[Classifier]])
  }

  def deleteClassifiers(classifierId : String) : Future[HttpResponse] = {
    val url = Option(classifierId) match {
      case Some(id) if id.nonEmpty => CLASSIFIER_URL + "/" + id
      case _ => throw new IllegalArgumentException("Classifier id cannot be empty")
    }

    val response : Future[HttpResponse] = send(Delete(endpoint + url))
    response
  }

  def getClassifier(classifierId : String) : Future[Classifier] = {
    val url = Option(classifierId) match {
      case Some(id) if id.nonEmpty => CLASSIFIER_URL + "/" + id
      case _ => throw new IllegalArgumentException("Classifier id cannot be empty")
    }

    val response : Future[HttpResponse] = send(Get(endpoint + url))

    response.map(unmarshal[Classifier])
  }
}
