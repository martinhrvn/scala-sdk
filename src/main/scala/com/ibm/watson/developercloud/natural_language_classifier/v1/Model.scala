package com.ibm.watson.developercloud.natural_language_classifier.v1

import spray.json._

case class Classification (var id: String, var url: String, var text: String, var topClass: String, var classes:  List[ClassifiedClass])

case class ClassifiedClass (var name: String, var confidence: Double)

case class TrainingData (var text: String, var classes: List[String])

case class Classifier (var id : String, var url: String, var status: String, var statusDescription: String)

object NaturalLanguageClassifierProtocol extends DefaultJsonProtocol {
  implicit val classifierFormat = jsonFormat(Classifier, "classifier_id", "url", "status", "status_description")
  implicit val classifiedClassFormat = jsonFormat(ClassifiedClass, "name", "confidence")
  implicit val classificationFormat = jsonFormat(Classification, "classification_id", "url", "text", "top_class", "classes")
  implicit val trainingDataFormat = jsonFormat(TrainingData, "text", "classes")
}