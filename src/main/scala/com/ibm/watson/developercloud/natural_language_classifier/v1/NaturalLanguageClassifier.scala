package com.ibm.watson.developercloud.natural_language_classifier.v1

import akka.actor.ActorSystem
import com.ibm.watson.developercloud.natural_language_classifier.v1.NaturalLanguageClassifierProtocol._
import com.ibm.watson.developercloud.utils.WatsonService
import com.typesafe.scalalogging.LazyLogging
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json._

import scala.concurrent.Future



class NaturalLanguageClassifier(var username: String, var password: String) extends WatsonService(username, password) with LazyLogging {
  implicit val system = ActorSystem()
  import system.dispatcher

  val ENDPOINT = "https://gateway.watsonplatform.net/natural-language-classifier-experimental/api"
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

    val pipeline: HttpRequest => Future[Classifier] = sendReceive ~> unmarshal[Classifier]

    val response: Future[Classifier] = pipeline(Post(ENDPOINT + CLASSIFIER_URL, jsonRequest.toString()))

    response
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
    val pipeline : HttpRequest => Future[Classification] = sendReceive ~> unmarshal[Classification]
    val response: Future[Classification] = pipeline(Post(ENDPOINT + url, jsonRequest.toString()))

    response
  }

  def getClassifiers : Future[List[Classifier]] = {
    logger.info("Running getClassifiers method");
    val pipeline : HttpRequest => Future[List[Classifier]] = sendReceive ~> unmarshal[List[Classifier]]
    val response: Future[List[Classifier]] = pipeline(Get(ENDPOINT + CLASSIFIER_URL))
    logger.info("returning a response")
    response
  }

  def deleteClassifiers(classifierId : String) : Future[HttpResponse] = {
    val url = Option(classifierId) match {
      case Some(id) if id.nonEmpty => CLASSIFIER_URL + "/" + id
      case _ => throw new IllegalArgumentException("Classifier id cannot be empty")
    }

    val pipeline : HttpRequest => Future[HttpResponse] = sendReceive
    val response : Future[HttpResponse] = pipeline(Delete(ENDPOINT + url))
    response
  }

  def getClassifier(classifierId : String) : Future[Classifier] = {
    val url = Option(classifierId) match {
      case Some(id) if id.nonEmpty => CLASSIFIER_URL + "/" + id
      case _ => throw new IllegalArgumentException("Classifier id cannot be empty")
    }

    val pipeline : HttpRequest => Future[Classifier] = sendReceive ~> unmarshal[Classifier]
    val response : Future[Classifier] = pipeline(Get(ENDPOINT + url))

    response
  }
}
