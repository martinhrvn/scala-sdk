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

import com.ibm.watson.developer_cloud.concept_insights.v2.ConceptInsights._
import com.ibm.watson.developer_cloud.concept_insights.v2.model.ConceptInsightsProtocol._
import com.ibm.watson.developer_cloud.concept_insights.v2.model._
import com.ibm.watson.developer_cloud.service.{ConfigFactory, VCAPConfigFactory, WatsonService}
import com.ibm.watson.developer_cloud.utils.Validation
import spray.client.pipelining._
import spray.http.{HttpResponse, Uri}
import spray.json._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future

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

    def annotateText(graph: Graph, text: String): Future[Annotations] = {
        val accId: String = getAccountId
        val graphId = IDHelper.graphId(graph, accId)

        val request = Post(apiVersion + graphId + annotateTextPath, text)

        val response = send(request)

        response.map(unmarshal[Annotations])
    }


    def conceptualSearch(
                          corpus: Corpus, cursor: Option[Int] = None, limit: Option[Int] = None,
                          ids: Option[List[String]] = None, conceptFields: Option[RequestedFields] = None,
                          documentFields: Option[RequestedFields] = None
                        ):
    Future[QueryConcepts] = {
        Validation.notNull(ids)
        val corpusId: String = IDHelper.corpusId(corpus, getAccountId)
        val cursorMap = cursor.map(i => "cursor" -> i.toString).toMap
        val limitMap = limit.map(i => "limit" -> i.toString).toMap
        val idsMap = ids.map(x => "ids" -> x.toJson.compactPrint).toMap
        val conceptMap = conceptFields.map(concept => "concept_fields" -> concept.fields.toJson.compactPrint).toMap
        val documentMap = documentFields.map(document => "document_fields" -> document.fields.toJson.compactPrint).toMap

        val queryParam = cursorMap ++ limitMap ++ idsMap ++ conceptMap ++ documentMap

        val request = Get(Uri(apiVersion + corpusId + conceptualSearchPath).withQuery(queryParam))
        val response = send(request)
        response.map(unmarshal[QueryConcepts])
    }

    def createCorpus(corpus: Corpus): Future[HttpResponse] = {
        val corpusId = IDHelper.corpusId(corpus, getAccountId)
        val request = Put(apiVersion + corpusId, corpus.toJson.compactPrint)
        val response = send(request)
        response
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

    def withAccountId(accountId: String): ConceptInsights = {
        new ConceptInsights(Some(accountId), configFactory)
    }
}

object ConceptInsights {
    val idsLabel: String = "ids"

    val apiVersion = "v2"
    val annotateTextPath = "/annotate_text"
    val conceptualSearchPath = "/conceptual_search"
}
