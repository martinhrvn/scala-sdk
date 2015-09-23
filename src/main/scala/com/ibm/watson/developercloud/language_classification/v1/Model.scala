package com.ibm.watson.developercloud.language_classification.v1

import spray.json.DefaultJsonProtocol

case class IdentifiedLanguage(id: String, confidence: Double)

object LanguageIdentificationProtocol extends DefaultJsonProtocol {
  implicit val identifiedFormat = jsonFormat(IdentifiedLanguage, "id", "confidence")
}