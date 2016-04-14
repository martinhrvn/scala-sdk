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
package com.ibm.watson.developer_cloud.concept_insights.v2.model

import com.ibm.watson.developer_cloud.utils.DateJsonFormat.DateJsonFormat
import spray.json.DefaultJsonProtocol
/**
  * Created by Martin Harvan on 12/04/16.
  */
object ConceptInsightsProtocol extends DefaultJsonProtocol {
    implicit val dateJsonFormat = DateJsonFormat
    implicit val permissionEnumProtocol = jsonFormat(Permission.apply _, "name")
    implicit val accountPermission = jsonFormat(AccountPermission, "account_id", "permission", "uid")
    implicit val accountProtocol = jsonFormat(Account, "account_id")
    implicit val accountsProtocol = jsonFormat(Accounts, "accounts")
    implicit val conceptProtocol = jsonFormat(Concept.apply, "abstract", "id", "label", "link", "ontology", "thumbnail", "type")
    implicit val scoredConceptProtocol = jsonFormat(ScoredConcept, "concept", "score")
    implicit val annotationProtocol = jsonFormat(Annotation, "concept", "part_index", "score", "text_index")
    implicit val annotationsProtocol = jsonFormat(Annotations, "annotations")
    implicit val buildStatusProtocol = jsonFormat(BuildStatus, "error", "processing", "ready")
    implicit val conceptsProtocol = jsonFormat(Concepts, "concepts")
    implicit val conceptMetadataProtocol = jsonFormat(ConceptMetadata, "abstract", "id", "label", "link", "ontology", "thumbnail", "type")
    implicit val corpusProtocol = jsonFormat(Corpus, "access", "account_permission", "id", "name", "ttl_hours", "expires_on")
    implicit val corpusProcessingStatusProtocol = jsonFormat(CorpusProcessingStatus, "build_status", "documents", "id", "last_updated")
    implicit val tagFormat = jsonFormat(Tag, "concept", "count")
    implicit val topTagsFormat = jsonFormat(TopTags, "corpus_tag_histogram", "document_length_histogram", "documents", "document_tag_histogram",
        "tags", "total_tags", "unique_tags")
    implicit val corpusStatsFormat = jsonFormat(CorpusStats, "id", "last_updated", "top_tags")
    implicit val corporaFormat = jsonFormat(Corpora, "corpora")
    implicit val resultFormat = jsonFormat(Result, "explanation_tags", "id", "label", "score")
    implicit val queryConceptsProtocol = jsonFormat(QueryConcepts.apply, "query_concepts","results")

}
