package com.ibm.watson.developer_cloud.personality_insights.v2.model

import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Created by martinhrvn on 20/03/16.
  */
object PersonalityInsightsProtocol extends DefaultJsonProtocol {
  implicit val traitFormat : JsonFormat[Trait] = lazyFormat(jsonFormat(Trait, "category", "children", "id", "name",
    "percentage", "raw_sampling_error", "raw_score", "sampling_error"))
  implicit val profileFormat = jsonFormat(Profile, "id", "processed_language", "source","tree", "word_count", "word_count_message")
}
