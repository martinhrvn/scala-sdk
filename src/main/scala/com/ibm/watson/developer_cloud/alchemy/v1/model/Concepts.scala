package com.ibm.watson.developer_cloud.alchemy.v1.model

import com.ibm.watson.developer_cloud.service.GenericModel

/**
  * Created by martinhrvn on 06/04/16.
  */
case class Concepts(concepts: List[Concept], totalTransactions: Integer, language: String, url: String)
  extends AlchemyLanguageGenericModel

case class Concept(census: String, ciaFactbook: String, crunchbase: String,
              dbpedia: String, freebase: String, geo: String, geonames: String,
              opencyc: String, relevance: String, text: String, website: String,
              yago: String) extends GenericModel