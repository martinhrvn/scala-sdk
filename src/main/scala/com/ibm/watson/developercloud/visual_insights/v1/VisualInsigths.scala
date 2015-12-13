package com.ibm.watson.developercloud.visual_insights.v1

import java.io.File

import com.ibm.watson.developercloud.utils.{Validation, WatsonService, WatsonServiceConfig}
import com.typesafe.scalalogging.LazyLogging
import spray.client.pipelining._
import spray.http._
import spray.json.{JsObject, JsString, JsValue}
import spray.httpx.SprayJsonSupport._
import VisualInsigthsProtocol._
import scala.concurrent.Future

/**
  * Created by martinhrvn on 13/12/15.
  */
class VisualInsigths(config: WatsonServiceConfig) extends WatsonService(config: WatsonServiceConfig) {
  /**
    * Gets the service type for service (used to get correct entry from VCAP_SERVICES properties)
    * @return
    */
  override def serviceType: String = "visual_insights"
  import system.dispatcher


  def getClassifiers : Future[Classifiers] = {
    return getClassifiers(null)
  }

  def getClassifiers(name: String) : Future[Classifiers] = {
    var uri = Uri(config.endpoint + VisualInsights.ClassifiersPath)
    uri = Option(name) match {
      case Some(n) if n.nonEmpty => uri.withQuery(VisualInsights.FilterName -> n)
      case _ => uri
    }
    val request = Get(uri)
    val response = send(request)
    response.map(unmarshal[Classifiers])
  }

  def getSummary(imagesFile: File) : Future[Summary] = {
    Validation.fileExists(imagesFile, "imagesFile cannot be null or empty");
    val data: List[BodyPart] = List(BodyPart(imagesFile, VisualInsights.ImagesFile))
    val formData = MultipartFormData(data)
    val request = Post(VisualInsights.SummaryPath, formData)
    val response = send(request)
    response.map(unmarshal[Summary])
  }
}

object VisualInsights {
  val ClassifiersPath = "/v1/classifiers"
  val FilterName = "filter_name"
  val ImagesFile = "images_file"
  val SummaryPath = "/v1/summary"
}
