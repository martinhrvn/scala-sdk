package com.ibm.watson.developercloud.personality_insights.v2

import com.ibm.watson.developercloud.utils.{Validation, WatsonService, WatsonServiceConfig}
import spray.client.pipelining._
import spray.http.HttpHeaders._
import spray.http._
import scala.concurrent.Future
import spray.httpx.SprayJsonSupport._
import com.ibm.watson.developercloud.personality_insights.v2.PersonalityInsightsProtocol._

/**
  * Created by martinhrvn on 11/12/15.
  */
class PersonalityInsights(config: WatsonServiceConfig) extends WatsonService(config: WatsonServiceConfig){
  import system.dispatcher
  override def serviceType: String = "personality_insights"
  val PATH_PROFILE = "/v2/profile"

  private def getRequest(options: ProfileOptions) : HttpRequest = {
    Validation.notNull(options, "Options cannot be null")
    Validation.assertTrue(options.text != null || options.contentItems != null, "text, html or content items need to be specified")
    var uri = Uri(endpoint + PATH_PROFILE)
    val text = options match {
      case o if o.text != null => o.text
      case _ => Content(options.contentItems).toString
    }

    uri = Option(options.includeRaw) match {
      case Some(incl) => uri.withQuery("include_raw"-> options.includeRaw.toString)
      case _ => uri
    }

    var request: HttpRequest = Post(uri, text).withHeaders(RawHeader("Content-Type", options.contentType))

    request = Option(options.language) match {
      case Some(lang) => request.withHeaders(RawHeader("Content-Language", lang.value))
      case _ => request
    }

    request = Option(options.acceptLanguage) match {
      case Some(lang) => request.withHeaders(RawHeader("Accept-Language", lang.value))
      case _ => request
    }

    request

  }

  def getProfile(text: String) : Future[Profile] = {
    val options = ProfileOptions(text)
    getProfile(options)

  }

  def getProfile(options: ProfileOptions) : Future[Profile] = {
    val request = getRequest(options)
    val response = send(request)
    response.map(unmarshal[Profile])
  }
}
