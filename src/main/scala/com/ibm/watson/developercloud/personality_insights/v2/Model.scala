// Copyright (C) 2015 IBM Corp. All Rights Reserved.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.ibm.watson.developercloud.personality_insights.v2

import java.util.Date

import spray.json.{JsonFormat, DefaultJsonProtocol}

case class Content(contentItems: List[ContentItem])

case class ContentItem(charset: String, content: String, contentType: String, created: Date, forward: Boolean,
                       id: String, language: String, parentId: String, reply: Boolean, sourceId: String,
                       update: Date, userId: String)

case class Profile(id: String, processedLanguage: String, source: String, tree: Trait, wordCount: Int, wordCountMessage: String)
case class ProfileOptions(text: String, contentType: Option[String] = None, contentItems: List[ContentItem] = List(),
                          includeRaw: Boolean = false, acceptLanguage: Option[AcceptLanguage] = None, language: Option[Language] = None)

case class Trait(category: String, children: List[Trait], id: String, name: String, percentage: Double,
                 rawSamplingError: Double, rawScore: Double, samplingError: Double)

case class Language(value: String)
case class AcceptLanguage(value: String)

object PersonalityInsightsProtocol extends DefaultJsonProtocol {
  implicit val traitFormat : JsonFormat[Trait] = lazyFormat(jsonFormat(Trait, "category", "children", "id", "name",
    "percentage", "raw_sampling_error", "raw_score", "sampling_error"))
  implicit val profileFormat = jsonFormat(Profile, "id", "processed_language", "source","tree", "word_count", "word_count_message")
}

