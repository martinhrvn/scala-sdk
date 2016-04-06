package com.ibm.watson.developer_cloud.alchemy.v1

/**
  * Created by martinhrvn on 20/03/16.
  */
case class Article(author: String, enrichedTitle: EnrichedTitle, publicationDate: PublicationDate, title: String, url: String)
case class EnrichedTitle(concepts: List[Concept], entities: List[Entity], sentiment: Sentiment, taxonomy: List[Taxonomy])