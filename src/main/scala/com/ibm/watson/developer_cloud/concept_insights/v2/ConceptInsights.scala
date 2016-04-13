// Copyright (C) 2016 IBM Corp. All Rights Reserved.
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
package com.ibm.watson.developer_cloud.concept_insights.v2

import com.ibm.watson.developer_cloud.concept_insights.v2.model._
import com.ibm.watson.developer_cloud.service.{ConfigFactory, VCAPConfigFactory, WatsonService}
import spray.json._
import spray.httpx.SprayJsonSupport
import com.ibm.watson.developer_cloud.concept_insights.v2.model.ConceptInsightsProtocol._

import scala.concurrent.Future
import spray.client.pipelining._
import spray.http.{BodyPart, HttpResponse, MultipartFormData, Uri}
import ConceptInsights._
import com.ibm.watson.developer_cloud.utils.Validation
import spray.httpx.SprayJsonSupport._
/**
  * Created by Martin Harvan on 11/04/16.
  */
class ConceptInsights(accountId: Option[String] = None, configFactory: ConfigFactory = new VCAPConfigFactory) extends WatsonService(configFactory) {
    /**
      * Gets the service type for service (used to get correct entry from VCAP_SERVICES properties)
      *
      * @return
      */
    override def serviceType: String = "concept_insights"

    def annotateText(graph: Graph, text: String) : Future[Annotations] = {
        val accId : String = getAccountId
        val graphId = IDHelper.graphId(graph, accId)

        val request = Post(apiVersion + graphId + annotateTextPath, text)

        val response = send(request)

        response.map(unmarshal[Annotations])
    }



    def conceptualSearch(corpus: Corpus, parameters: Map[String, Any]) : QueryConcepts = {
        Validation.notNull(parameters("ids"))
        val corpusId : String = IDHelper.corpusId(corpus, getAccountId)

        val ids : Map[String, Any] = parameters("ids") match {
             case idList : List[String] => Map("ids" -> idList.toJson)
             case _ => throw new IllegalArgumentException("parameters('ids') should be List[String]")
         }

        val conceptFields: Map[String, Map[String, Int]] = Option(parameters("concept_fields")) match {
            case Some(fields : RequestedFields) => Map("concept_fields" -> fields.fields)
            case _ => Map.empty[String, Map[String, Int]]
        }

        val documenttFields: Map[String, Map[String, Int]] = Option(parameters("document_fields")) match {
            case Some(fields : RequestedFields) => Map("document_fields" -> fields.fields)
            case _ => Map.empty[String, Map[String, Int]]
        }

        val queryParam = parameters.filter(p => p._1.equals("cursor") || p._1.equals("limit")) ++ conceptFields ++ ids ++ documenttFields

        val request = Get(apiVersion + corpusId + conceptualSearchPath)



    }

    def getAccountId: String = {
        accountId match {
            case Some(id) if id.nonEmpty => id
            case _ => getAccountsInfo.value.get.get.accounts.head.id //TODO error handling
        }
    }

    def getAccountsInfo: Future[Accounts] = {
        val request = Get("path")
        val response = send(request)
        response.map(unmarshal[Accounts])
    }

    def withAccountId(accountId: String) : ConceptInsights = {
        new ConceptInsights(Some(accountId), configFactory)
    }
}

object ConceptInsights {
    val apiVersion = "v2"
    val annotateTextPath = "/annotate_text"
    val conceptualSearchPath = "/conceptual_search"
}
