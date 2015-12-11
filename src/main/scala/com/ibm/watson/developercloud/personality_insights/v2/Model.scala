package com.ibm.watson.developercloud.personality_insights.v2

import java.util.Date

import spray.json.{JsonFormat, DefaultJsonProtocol}

case class Content(contentItems: List[ContentItem])

case class ContentItem(charset: String, content: String, contentType: String, created: Date, forward: Boolean,
                       id: String, language: String, parentId: String, reply: Boolean, sourceId: String,
                       update: Date, userId: String)

case class Profile(id: String, processedLanguage: String, source: String, tree: Trait, wordCount: Int, wordCountMessage: String)
case class ProfileOptions(text: String, contentType: String = null, contentItems: List[ContentItem] = null, includeRaw: Boolean = false, acceptLanguage: AcceptLanguage = null, language: Language = null)

case class Trait(category: String, children: List[Trait], id: String, name: String, percentage: Double, rawSamplingError: Double, rawScore: Double, samplingError: Double)

case class Language(value: String);
case class AcceptLanguage(value: String)

object PersonalityInsightsProtocol extends DefaultJsonProtocol {
  implicit val traitFormat : JsonFormat[Trait] = lazyFormat(jsonFormat(Trait, "category", "children", "id", "name", "percentage", "raw_sampling_error", "raw_score", "sampling_error"))
  implicit val profileFormat = jsonFormat(Profile, "id", "processed_language", "source","tree", "word_count", "word_count_message")
}

