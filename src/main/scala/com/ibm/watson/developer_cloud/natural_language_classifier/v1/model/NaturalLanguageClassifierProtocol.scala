package com.ibm.watson.developer_cloud.natural_language_classifier.v1.model

import spray.json.DefaultJsonProtocol
/**
  * Created by martinhrvn on 20/03/16.
  */
object NaturalLanguageClassifierProtocol extends DefaultJsonProtocol {
  implicit val classifierFormat = jsonFormat(Classifier, "classifier_id", "url", "status", "status_description")
  implicit val classifiedClassFormat = jsonFormat(ClassifiedClass, "name", "confidence")
  implicit val classificationFormat = jsonFormat(Classification, "classification_id", "url", "text", "top_class", "classes")
  implicit val trainingDataFormat = jsonFormat(TrainingData, "text", "classes")
}
