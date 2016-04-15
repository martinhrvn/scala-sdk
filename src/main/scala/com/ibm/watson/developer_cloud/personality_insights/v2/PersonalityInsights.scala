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
package com.ibm.watson.developer_cloud.personality_insights.v2

import com.ibm.watson.developer_cloud.personality_insights.v2.model.{ProfileOptions, Profile, Content, PersonalityInsightsProtocol}
import PersonalityInsightsProtocol._
import com.ibm.watson.developer_cloud.service.{VCAPConfigFactory, ConfigFactory, WatsonService}
import com.ibm.watson.developer_cloud.utils._
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http.HttpHeaders._
import spray.http._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future

/**
  * The Watson Personality Insights service uses linguistic analytics to extract a spectrum of
  * cognitive and social characteristics from the text data that a person generates through blogs,
  * tweets, forum posts, and more.
  *
  * @version v2
  */
class PersonalityInsights(configFactory: ConfigFactory = new VCAPConfigFactory()) extends WatsonService(configFactory){
  val logger = LoggerFactory.getLogger(classOf[PersonalityInsights])
  def serviceType: String = "personality_insights"

  private[this] def getRequest(options: ProfileOptions) : HttpRequest = {
    Validation.notNull(options, "Options cannot be null")
    Validation.assertTrue(Validation.notNull(options.text) || Validation.notNull(options.contentItems), "text, html or content items need to be specified")
    val inUri = Uri(config.endpoint + PersonalityInsights.pathProfile)
    val text = Option(options.text) match {
      case Some(o) => o
      case _ => Content(options.contentItems).toString
    }

    val uri = Option(options.includeRaw).map(x => inUri.withQuery(PersonalityInsights.includeRaw -> x.toString)).getOrElse(inUri)

    val headers = options.contentType.map(p => RawHeader(WatsonService.contentType, p)).toList ++
    options.language.map(p => RawHeader(WatsonService.contentLanguage, p.value)).toList ++
    options.acceptLanguage.map(p => RawHeader(WatsonService.acceptLanguage, p.value)).toList
    val request = Post(uri, text).withHeaders(headers)

    request
  }

  /**
    * Accepts text and responds with Profile with a tree of characteristics that include
    * personality, needs and values.
    *
    * @param text Text to analyze
    * @return The personality Profile
    */
  def getProfile(text: String) : Future[Profile] = {
    val options = ProfileOptions(text)
    getProfile(options)

  }

  /**
    * Accepts text and responds with Profile with a tree of characteristics that include
    * personality, needs and values.
    *
    * @param options profile options
    * @return The personality Profile
    */
  def getProfile(options: ProfileOptions) : Future[Profile] = {
    val request = getRequest(options)
    val response = send(request)
    response.map(unmarshal[Profile])
  }
}

object PersonalityInsights {
  val includeRaw = "include_raw"
  val pathProfile = "/v2/profile"

}
