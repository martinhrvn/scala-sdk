package com.ibm.watson.developer_cloud.language_translation.v2.model

import java.io.File

/**
  * Created by martinhrvn on 20/03/16.
  */
case class CreateModelOptions(baseModelId: String, forcedGlossary: File, monlingualCorpus: File, name: String, parallelCorpus: File)
