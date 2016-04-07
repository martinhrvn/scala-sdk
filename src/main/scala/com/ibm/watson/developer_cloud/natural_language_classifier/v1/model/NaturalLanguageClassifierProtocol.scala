package com.ibm.watson.developer_cloud.natural_language_classifier.v1.model

import com.ibm.watson.developer_cloud.utils.DateJsonFormat.DateJsonFormat
import spray.json.DefaultJsonProtocol

/**
  * Created by martinhrvn on 20/03/16.
  */
object NaturalLanguageClassifierProtocol extends DefaultJsonProtocol {
  implicit val dateJsonFormat = DateJsonFormat
  implicit val classifierFormat = jsonFormat(Classifier, "created","classifier_id", "language","name", "status", "status_description", "url" )
  implicit val classifiersFormat = jsonFormat(Classifiers, "classifiers" )
  implicit val classifiedClassFormat = jsonFormat(ClassifiedClass, "class_name", "confidence")
  implicit val classificationFormat = jsonFormat(Classification, "classifier_id", "url", "text", "top_class", "classes")
  implicit val trainingDataFormat = jsonFormat(TrainingData, "text", "classes")
}
