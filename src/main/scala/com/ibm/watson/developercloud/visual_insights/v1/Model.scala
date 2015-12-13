package com.ibm.watson.developercloud.visual_insights.v1

import spray.json.DefaultJsonProtocol

case class Classifier(name: String)
case class Classifiers(classifiers : List[Classifier])
case class Summary(name: String, score: Double)

object VisualInsigthsProtocol extends DefaultJsonProtocol {
  implicit val classifierFormat = jsonFormat(Classifier, "name")
  implicit val classifiersFormat = jsonFormat(Classifiers, "classifiers")
  implicit val summaryFormat = jsonFormat(Summary, "name", "score")
}