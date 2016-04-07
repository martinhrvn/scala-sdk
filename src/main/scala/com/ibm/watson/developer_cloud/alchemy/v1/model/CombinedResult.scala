package com.ibm.watson.developer_cloud.alchemy.v1.model

/**
  * Created by martinhrvn on 20/03/16.
  */
case class CombinedResult(author: String, concepts: List[Concept],
                          entities: List[Entity], feeds: List[Feed], image: String,
                         imageKeywords: List[Keyword], keywords: List[Keyword],
                         publicationDste: PublicationDate, relations: List[SAORelation],
                         sentiment: Sentiment, taxonomy: List[Taxonomy], title: String, totalTransactions: Integer, url: String, language: String ) extends AlchemyLanguageGenericModel
