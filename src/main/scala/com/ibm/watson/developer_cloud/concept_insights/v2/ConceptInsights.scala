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
  *
  * Created by Martin Harvan on 11/04/16.
  */
class ConceptInsights(accountId: Option[String] = None, configFactory: ConfigFactory = new VCAPConfigFactory) extends WatsonService(configFactory) {
    private lazy val accId = accountId match {
        case Some(id) if id.nonEmpty => id
        case _ => getAccountsInfo.value.get.get.accounts.head.id //TODO error handling
    }

    /**
      * Gets the service type for service (used to get correct entry from VCAP_SERVICES properties)
      *
      * @return service type
      */
    override def serviceType: String = "concept_insights"

    /**
      *
      * @param graph
      * @param text
      * @return
      */
    def annotateText(graph: Graph, text: String): Future[Annotations] = {
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
        val corpusId: String = IDHelper.corpusId(corpus, accId)
        val cursorMap = cursor.map(i => cursorLabel -> i.toString).toMap
        val limitMap = limit.map(i => limitLabel -> i.toString).toMap
        val idsMap = ids.map(x => idsLabel -> x.toJson.compactPrint).toMap
        val conceptMap = conceptFields.map(concept => conceptFieldsLabel -> concept.fields.toJson.compactPrint).toMap
        val documentMap = documentFields.map(document => "document_fields" -> document.fields.toJson.compactPrint).toMap

        val queryParam = cursorMap ++ limitMap ++ idsMap ++ conceptMap ++ documentMap

        val request = Get(Uri(apiVersion + corpusId + conceptualSearchPath).withQuery(queryParam))
        val response = send(request)
        response.map(unmarshal[QueryConcepts])
    }

    def createCorpus(corpus: Corpus): Future[HttpResponse] = {
        val corpusId = IDHelper.corpusId(corpus, accId)
        val request = Put(apiVersion + corpusId, corpus.toJson.compactPrint)
        val response = send(request)
        response
    }

    def createDocument(document: Document) : Future[HttpResponse] = {
        val documentId = IDHelper.documentId(document)

        val request = Put(apiVersion + documentId, document.toJson.compactPrint)

        send(request)
    }

    def deleteCorpus(corpus: Corpus) : Future[HttpResponse] = {
        val corpusId = IDHelper.corpusId(corpus, accId)
        val request = Delete(apiVersion + corpusId)

        send(request)
    }

    def deleteDocument(document: Document) : Future[HttpResponse] = {
        val documentId = IDHelper.documentId(document)
        val request = Delete(apiVersion + documentId)

        send(request)
    }

    def getConcept(concept: Concept) : Future[ConceptMetadata] = {
        val conceptId : String = IDHelper.conceptId(concept)
        val request = Get(apiVersion + conceptId)

        val response = send(request)

        response.map(unmarshal[ConceptMetadata])
    }


    def getConceptRelatedConcepts(concept: Concept, conceptFields: Option[QueryConcepts], level: Option[Int], limit:
    Option[Int]) : Future[Concepts] = {
        val conceptId = IDHelper.conceptId(concept)
        val limitMap = limit.map(i => limitLabel -> i.toString).toMap
        val levelMap = level.map(i => levelLabel -> i.toString).toMap
        val queryConceptMap = conceptFields.map(query => conceptFieldsLabel -> query.toJson.compactPrint).toMap

        val queryParams = limitMap ++ levelMap ++ queryConceptMap

        val request = Get(Uri(apiVersion + conceptId + relatedConceptsPath).withQuery(queryParams))
        val response = send(request)

        response.map(unmarshal[Concepts])
    }

    def getDocumentRelatedConcepts(document: Document,conceptFields: Option[QueryConcepts], level: Option[Int],
    limit: Option[Int]): Future[Concepts] = {
        val documentId = IDHelper.documentId(document)
        val limitMap = limit.map(i => limitLabel -> i.toString).toMap
        val levelMap = level.map(i => levelLabel -> i.toString).toMap
        val queryConceptMap = conceptFields.map(query => conceptFieldsLabel -> query.toJson.compactPrint).toMap

        val queryParams = limitMap ++ levelMap ++ queryConceptMap

        val request = Get(Uri(apiVersion + documentId + relatedConceptsPath).withQuery(queryParams))
        val response = send(request)

        response.map(unmarshal[Concepts])

    }
    def getCorpusStats(corpus: Corpus): Future[CorpusStats] = {
        val corpusId = IDHelper.corpusId(corpus, accId)

        val request = Get(apiVersion + corpusId + statsPath)
        send(request).map(unmarshal[CorpusStats])
    }

    def getDocument(document: Document): Future[Document] = {
        val documentId = IDHelper.documentId(document)

        val request = Get(apiVersion + documentId)

        send(request).map(unmarshal[Document])
    }

    def getDocumentAnnotations(document: Document): Future[Any] = {
        val documentId = IDHelper.documentId(document)

        val request = Get(apiVersion + documentId + annotationsPath)

        send(request).map(unmarshal[DocumentAnnotations])
    }

    def getDocumentProcessingStatus(document: Document): Future[DocumentProcessingStatus] = {
        val documentId = IDHelper.documentId(document)
        val request = Get(apiVersion + documentId + processingStatusPath)

        send(request).map(unmarshal[DocumentProcessingStatus])
    }

    def getDocumentRelationScores(document: Document, concepts: List[Concept]): Future[Scores] = {
        Validation.notEmpty(concepts, "concepts cannot be empty")
        val documentId = IDHelper.documentId(document)

        val conceptsJson = JsArray(concepts.map(c => c.id.toJson).toVector)
        val queryParam = Map("concepts" -> JsObject("concepts" -> conceptsJson).compactPrint)

        val request = Get(Uri(apiVersion + documentId + relationScoresPath).withQuery(queryParam))

        send(request).map(unmarshal[Scores])
    }

    def listCorpora(accountId: String): Future[Corpora] = {
        Validation.notEmpty(accountId, "account_id cannot be empty")
        val request = Get("/v2/corpora/" + accountId)
        send(request).map(unmarshal[Corpora])
    }

    def listDocuments(corpus: Corpus, cursor: Option[Int], limit: Option[Int], query: Option[String]): Future[Documents] = {
        val corpusId = IDHelper.corpusId(corpus, accId)

        val cursorMap = cursor.map(c => cursorLabel -> c.toString).toMap
        val limitMap = limit.map(i => cursorLabel -> i.toString).toMap
        val queryMap = query.map(s => "query" -> s).toMap

        val queryParams = cursorMap ++ limitMap ++ queryMap

        val request = Get(Uri(apiVersion + corpusId + documentsPath).withQuery(queryParams))

        send(request).map(unmarshal[Documents])
    }

    def searchCorpusByLabel(corpus: Corpus, conceptFields: Option[RequestedFields], documentFields:
    Option[RequestedFields], query: String, prefix: Option[Boolean], limit: Option[Int], concepts: Option[Boolean]):
    Future[Matches]
    = {
        val corpusId = IDHelper.corpusId(corpus, accId)

        Validation.notEmpty(query, "query cannot be empty")
        val limitMap = limit.map(i => limitLabel -> i.toString).toMap
        val prefixMap = prefix.map(s => "prefix" -> s.toString).toMap
        val conceptMap = concepts.map(s => "concept" -> s.toString)
        val queryMap = Map("query" -> query)
        val conceptFieldsMap = conceptFields.map(field => conceptFieldsLabel -> field.fields.toJson.compactPrint).toMap
        val documentFieldsMap = documentFields.map(field => "document_fields" -> field.fields.toJson.compactPrint).toMap

        val queryParams = limitMap ++ prefixMap ++ queryMap ++ conceptFieldsMap ++ conceptMap ++ documentFieldsMap

        val request = Get(Uri(apiVersion + corpusId + labelSearchPath).withQuery(queryParams))
        send(request).map(unmarshal[Matches])
    }

    def searchGraphsConceptByLabel(graph: Graph, requestedFields: Option[RequestedFields], query: String, prefix:
    Option[Boolean], limit: Option[Int]) : Future[Matches] = {
        val graphId = IDHelper.graphId(graph, accId)

        Validation.notEmpty(query, "query cannot be empty")
        val limitMap = limit.map(i => limitLabel -> i.toString).toMap
        val prefixMap = prefix.map(s => "prefix" -> s.toString).toMap
        val queryMap = Map("query" -> query)
        val conceptFieldsMap = requestedFields.map(field => conceptFieldsLabel -> field.fields.toJson.compactPrint).toMap

        val queryParams = limitMap ++ prefixMap ++ queryMap ++ conceptFieldsMap

        val request = Get(Uri(apiVersion + graphId + labelSearchPath).withQuery(queryParams))
        send(request).map(unmarshal[Matches])
    }

    def listGraphs: Future[Graph] = {
        val request = Get(apiVersion + graphsPath)
        send(request).map(unmarshal[Graph])
    }

    def updateCorpus(corpus: Corpus): Future[HttpResponse] = {
        val corpusId = IDHelper.corpusId(corpus, accId)
        val request = Post(apiVersion + corpusId, corpus.toJson.compactPrint)
        send(request)
    }

    def updateDocument(document: Document): Future[HttpResponse] = {
        val documentId = IDHelper.documentId(document)
        val request = Post(apiVersion + documentId, document.toJson.compactPrint)
        send(request)
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
    val apiVersion = "v2"
    val processingStatusPath = "/processing_state"
    val graphsPath = "/graphs"
    val documentsPath = "/documents"
    val labelSearchPath = "/label_search"
    val annotationsPath = "/annotations"
    val relatedConceptsPath = "/related_concepts"
    val statsPath = "/stats"
    val annotateTextPath = "/annotate_text"
    val conceptualSearchPath = "/conceptual_search"
    val relationScoresPath = "/relation_scores"
     val idsLabel: String = "ids"
     val limitLabel: String = "limit"
     val levelLabel: String = "level"
     val cursorLabel: String = "cursor"
     val conceptFieldsLabel: String = "concept_fields"
}
