package com.ibm.watson.developer_cloud.language_translation.v2.model

import java.io.File

import com.ibm.watson.developer_cloud.service.GenericModel

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 20/03/16.
  */
case class CreateModelOptions(baseModelId: String, forcedGlossary: File, monlingualCorpus: File, name: String, parallelCorpus: File)extends GenericModel
