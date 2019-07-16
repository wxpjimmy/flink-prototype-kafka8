package com.jimmy.tranquility

import java.io.File
import java.util.Properties

import com.jimmy.ad.exchange.thrift.model.BillingExtensions
import com.jimmy.flink.AdBillingExtentionDeserializedSchema
import io.druid.query.aggregation.DoubleSumAggregatorFactory
import net.sf.json.JSONObject

import scala.collection.JavaConversions._
import scala.util.Random

//import com.alibaba.fastjson.JSONObject
import com.google.common.base.Charsets
import com.google.common.io.Files
import io.druid.query.aggregation.{AggregatorFactory, CountAggregatorFactory, LongSumAggregatorFactory}
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger

import scala.collection.immutable.HashMap

object AdBillingExtensionService {

  val logger = Logger.getLogger(this.getClass)
  val random = new Random
  val witeAdIdSet = Set("10048295", "10051645", "10049026")
  val deserializer = new AdBillingExtentionDeserializedSchema
  val dimensions = Array("adid", "gender", "province", "age", "mediaType", "dsp", "platform", "city", "education",
    "incomeLevel", "tagId", "billingType", "experimentId", "reqType", "refType", "templateId", "sessionId", "pageId", "posId",
    "expAppId", "expLayer1", "expLayer2", "expLayer3", "expLayer4", "expLayer5", "expLayer6", "expLayer7", "expLayer8", "uaId",
    "upId", "mediaPackage", "placementId", "imei", "refPosition", "subAccountId", "subAccountType", "campaignId", "assetId", "dealId", "isH5",
    "expBotTypeValue", "env", "seq", "KafkaTopic", "webResVersion", "app", "appId", "expLayer9", "expLayer10", "expLayer11", "expLayer12",
    "expLayer13", "expLayer14", "expLayer15", "expLayer16", "expLayer17", "expLayer18", "expLayer19", "expLayer20", "expLayer21", "expLayer22",
    "expLayer23", "expLayer24", "expLayer25", "expLayer26", "expLayer27", "expLayer28", "expLayer29", "expLayer30", "expLayer31")

  val aggregators: Array[AggregatorFactory] = Array(
    new CountAggregatorFactory("count"),
    new LongSumAggregatorFactory("fee", "fee"),
    new LongSumAggregatorFactory("expose", "expose"),
    new LongSumAggregatorFactory("click", "click"),
    new LongSumAggregatorFactory("startDownload", "startDownload"),

    new LongSumAggregatorFactory("query", "query"),
    new LongSumAggregatorFactory("queryFailed", "queryFailed"),
    new LongSumAggregatorFactory("loaded", "loaded"),
    new LongSumAggregatorFactory("endDownload", "endDownload"),
    new LongSumAggregatorFactory("startInstall", "startInstall"),
    new LongSumAggregatorFactory("endInstall", "endInstall"),
    new LongSumAggregatorFactory("trueView", "trueView"),
    new DoubleSumAggregatorFactory("predict-ctr", "predict-ctr")
  )

  def buildService(properties: Properties): TranquilityService = {
    new TranquilityService(properties, dimensions, aggregators)
  }

  def process(billingExtensions: BillingExtensions): Map[String, String] = {
    if (billingExtensions == null || !billingExtensions.isSetTimestamp() || !billingExtensions.isSetAdid()) null
    else {
      //Only for staging debug
      logger.debug("Message: " + billingExtensions.toString());

      var columnMap = HashMap[String, String]()
      columnMap += (("timestamp", billingExtensions.getTimestamp()))
      columnMap += (("adid", billingExtensions.getAdid()))
      columnMap += (("gender", billingExtensions.getGender()))
      columnMap += (("province", billingExtensions.getProvince()))
      columnMap += (("age", billingExtensions.getAge()))
      columnMap += (("mediaType", billingExtensions.getMediaType()))
      columnMap += (("fee", billingExtensions.getFee()))
      columnMap += (("expose", billingExtensions.getExpose()))
      columnMap += (("click", billingExtensions.getClick()))
      columnMap += (("startDownload", billingExtensions.getStartDownload()))
      columnMap += (("trueView", billingExtensions.getTrueView().toString))
      if (billingExtensions.isSetDsp) {
        columnMap += (("dsp", billingExtensions.getDsp()))
      }
      if (billingExtensions.isSetPlatform) {
        columnMap += (("platform", billingExtensions.getPlatform()))
      }
      if (billingExtensions.isSetCity) {
        columnMap += (("city", billingExtensions.getCity()))
      }
      if (billingExtensions.isSetEducation) {
        columnMap += (("education", billingExtensions.getEducation()))
      }
      if (billingExtensions.isSetIncomeLevel) {
        columnMap += (("incomeLevel", billingExtensions.getIncomeLevel()))
      }
      if (billingExtensions.isSetTagId) {
        columnMap += (("tagId", billingExtensions.getTagId()))
      }
      if (billingExtensions.isSetBillingType) {
        columnMap += (("billingType", billingExtensions.getBillingType()))
      }
      if (billingExtensions.isSetExperimentId) {
        columnMap += (("experimentId", billingExtensions.getExperimentId()))
      }
      if (billingExtensions.isSetDealId && StringUtils.isNotBlank(billingExtensions.getDealId())) {
        columnMap += (("dealId", billingExtensions.getDealId()))
      }
      if (billingExtensions.isSetAppInfo && StringUtils.isNotBlank(billingExtensions.getAppInfo())) {
        columnMap += (("app", billingExtensions.getAppInfo()))
        columnMap += (("appId", billingExtensions.getAppInfo().split("-").apply(0)))
      }
      if (billingExtensions.isSetLogExtraInfo) {
        try {
          var jsonObj = JSONObject.fromObject(billingExtensions.getLogExtraInfo)
          if (jsonObj.containsKey("env")) {
            columnMap += (("env", jsonObj.get("env").toString))
          }
          if (jsonObj.containsKey("seq")) {
            columnMap += (("seq", jsonObj.get("seq").toString))
          }
          // 一点资讯的傻逼逻辑做适配，TODO remove it
          if (jsonObj.containsKey("extraParameters")) {
            try {
              jsonObj = jsonObj.getJSONObject("extraParameters")
              if (jsonObj.containsKey("env")) {
                columnMap += (("env", jsonObj.get("env").toString))
              }
              if (jsonObj.containsKey("seq")) {
                columnMap += (("seq", jsonObj.get("seq").toString))
              }
            }
          }
          if (jsonObj.containsKey("KafkaTopic")) {
            columnMap += (("KafkaTopic", jsonObj.get("KafkaTopic").toString))
          }
          if (jsonObj.containsKey("webResVersion")) {
            columnMap += (("webResVersion", jsonObj.get("webResVersion").toString))
          }
        }
      }

      columnMap += (("reqType", billingExtensions.reqType))
      columnMap += (("refType", billingExtensions.refType))
      columnMap += (("templateId", billingExtensions.templateId))
      columnMap += (("sessionId", billingExtensions.sessionId))
      columnMap += (("pageId", billingExtensions.pageId))
      columnMap += (("posId", billingExtensions.posId))
      columnMap += (("expAppId", billingExtensions.expAppId))
      columnMap += (("expLayer1", billingExtensions.expLayer1))
      columnMap += (("expLayer2", billingExtensions.expLayer2))
      columnMap += (("expLayer3", billingExtensions.expLayer3))
      columnMap += (("expLayer4", billingExtensions.expLayer4))
      columnMap += (("expLayer5", billingExtensions.expLayer5))
      columnMap += (("expLayer6", billingExtensions.expLayer6))
      columnMap += (("expLayer7", billingExtensions.expLayer7))
      columnMap += (("expLayer8", billingExtensions.expLayer8))
      columnMap += (("query", billingExtensions.query.toString))
      columnMap += (("queryFailed", billingExtensions.queryFailed.toString))
      columnMap += (("loaded", billingExtensions.loaded.toString))
      columnMap += (("endDownload", billingExtensions.endDownload.toString))
      columnMap += (("startInstall", billingExtensions.startInstall.toString))
      columnMap += (("endInstall", billingExtensions.endInstall.toString))
      columnMap += (("uaId", billingExtensions.uaId))
      columnMap += (("upId", billingExtensions.upId))
      columnMap += (("mediaPackage", billingExtensions.mediaPackage))
      columnMap += (("placementId", billingExtensions.placementId))
      columnMap += (("imei", ""))
      columnMap += (("refPosition", billingExtensions.refPosition))
      columnMap += (("subAccountId", billingExtensions.subAccountId))
      columnMap += (("subAccountType", billingExtensions.subAccountType))
      columnMap += (("campaignId", billingExtensions.campaignId))
      columnMap += (("assetId", billingExtensions.assetId.toString))
      columnMap += (("isH5", billingExtensions.isH5))
      columnMap += (("expBotTypeValue", billingExtensions.expBotTypeValue.toString))

      if (billingExtensions.isSetExtraExpLayer()) {
        billingExtensions.extraExpLayer.foreach(kv => {
          val key = "expLayer" + kv._1
          columnMap += ((key, kv._2.toString))
        })
      }
      if (billingExtensions.isSetLogExtraInfo()) {
        try {
          val extraObj = JSONObject.fromObject(billingExtensions.logExtraInfo)
          if (extraObj.containsKey("ctr") && StringUtils.isNotBlank(billingExtensions.getExpose()) && billingExtensions.getExpose.toLong > 0) {
            columnMap += (("predict-ctr", extraObj.getDouble("ctr").toString))
          }
        } catch {
          case ex: Exception => {
            logger.error("get predict-ctr failed exception: ", ex)
          }
          // ignore
        }
      }

      columnMap
    }
  }


  def process2(billingExtensions: BillingExtensions): java.util.Map[String, String] = {
    if (billingExtensions == null || !billingExtensions.isSetTimestamp() || !billingExtensions.isSetAdid()) null  //
    else {
      //Only for staging debug
      logger.debug("Message: " + billingExtensions.toString());

      var columnMap = new java.util.HashMap[String, String]()
      columnMap.put("timestamp", billingExtensions.getTimestamp())
      columnMap.put("adid", billingExtensions.getAdid())
      columnMap.put("gender", billingExtensions.getGender())
      columnMap.put("province", billingExtensions.getProvince())
      columnMap.put("age", billingExtensions.getAge())
      columnMap.put("mediaType", billingExtensions.getMediaType())
      columnMap.put("fee", billingExtensions.getFee())
      columnMap.put("expose", billingExtensions.getExpose())
      columnMap.put("click", billingExtensions.getClick())
      columnMap.put("startDownload", billingExtensions.getStartDownload())
      columnMap.put("trueView", billingExtensions.getTrueView().toString)
      if (billingExtensions.isSetDsp) {
        columnMap.put("dsp", billingExtensions.getDsp())
      }
      if (billingExtensions.isSetPlatform) {
        columnMap.put("platform", billingExtensions.getPlatform())
      }
      if (billingExtensions.isSetCity) {
        columnMap.put("city", billingExtensions.getCity())
      }
      if (billingExtensions.isSetEducation) {
        columnMap.put("education", billingExtensions.getEducation())
      }
      if (billingExtensions.isSetIncomeLevel) {
        columnMap.put("incomeLevel", billingExtensions.getIncomeLevel())
      }
      if (billingExtensions.isSetTagId) {
        columnMap.put("tagId", billingExtensions.getTagId())
      }
      if (billingExtensions.isSetBillingType) {
        columnMap.put("billingType", billingExtensions.getBillingType())
      }
      if (billingExtensions.isSetExperimentId) {
        columnMap.put("experimentId", billingExtensions.getExperimentId())
      }
      if (billingExtensions.isSetDealId && StringUtils.isNotBlank(billingExtensions.getDealId())) {
        columnMap.put("dealId", billingExtensions.getDealId())
      }
      if (billingExtensions.isSetAppInfo && StringUtils.isNotBlank(billingExtensions.getAppInfo())) {
        columnMap.put("app", billingExtensions.getAppInfo())
        columnMap.put("appId", billingExtensions.getAppInfo().split("-").apply(0))
      }
      if (billingExtensions.isSetLogExtraInfo) {
        try {
          var jsonObj = JSONObject.fromObject(billingExtensions.getLogExtraInfo)
          if (jsonObj.containsKey("env")) {
            columnMap.put("env", jsonObj.get("env").toString)
          }
          if (jsonObj.containsKey("seq")) {
            columnMap.put("seq", jsonObj.get("seq").toString)
          }
          // 一点资讯的傻逼逻辑做适配，TODO remove it
          if (jsonObj.containsKey("extraParameters")) {
            try {
              jsonObj = jsonObj.getJSONObject("extraParameters")
              if (jsonObj.containsKey("env")) {
                columnMap.put("env", jsonObj.get("env").toString)
              }
              if (jsonObj.containsKey("seq")) {
                columnMap.put("seq", jsonObj.get("seq").toString)
              }
            }
          }
          if (jsonObj.containsKey("KafkaTopic")) {
            columnMap.put("KafkaTopic", jsonObj.get("KafkaTopic").toString)
          }
          if (jsonObj.containsKey("webResVersion")) {
            columnMap.put("webResVersion", jsonObj.get("webResVersion").toString)
          }
        }
      }

      columnMap.put("reqType", billingExtensions.reqType)
      columnMap.put("refType", billingExtensions.refType)
      columnMap.put("templateId", billingExtensions.templateId)
      columnMap.put("sessionId", billingExtensions.sessionId)
      columnMap.put("pageId", billingExtensions.pageId)
      columnMap.put("posId", billingExtensions.posId)
      columnMap.put("expAppId", billingExtensions.expAppId)
      columnMap.put("expLayer1", billingExtensions.expLayer1)
      columnMap.put("expLayer2", billingExtensions.expLayer2)
      columnMap.put("expLayer3", billingExtensions.expLayer3)
      columnMap.put("expLayer4", billingExtensions.expLayer4)
      columnMap.put("expLayer5", billingExtensions.expLayer5)
      columnMap.put("expLayer6", billingExtensions.expLayer6)
      columnMap.put("expLayer7", billingExtensions.expLayer7)
      columnMap.put("expLayer8", billingExtensions.expLayer8)
      columnMap.put("query", billingExtensions.query.toString)
      columnMap.put("queryFailed", billingExtensions.queryFailed.toString)
      columnMap.put("loaded", billingExtensions.loaded.toString)
      columnMap.put("endDownload", billingExtensions.endDownload.toString)
      columnMap.put("startInstall", billingExtensions.startInstall.toString)
      columnMap.put("endInstall", billingExtensions.endInstall.toString)
      columnMap.put("uaId", billingExtensions.uaId)
      columnMap.put("upId", billingExtensions.upId)
      columnMap.put("mediaPackage", billingExtensions.mediaPackage)
      columnMap.put("placementId", billingExtensions.placementId)
      columnMap.put("imei", "")
      columnMap.put("refPosition", billingExtensions.refPosition)
      columnMap.put("subAccountId", billingExtensions.subAccountId)
      columnMap.put("subAccountType", billingExtensions.subAccountType)
      columnMap.put("campaignId", billingExtensions.campaignId)
      columnMap.put("assetId", billingExtensions.assetId.toString)
      columnMap.put("isH5", billingExtensions.isH5)
      columnMap.put("expBotTypeValue", billingExtensions.expBotTypeValue.toString)

      if (billingExtensions.isSetExtraExpLayer()) {
        billingExtensions.extraExpLayer.foreach(kv => {
          val key = "expLayer" + kv._1
          columnMap.put(key, kv._2.toString)
        })
      }
      if (billingExtensions.isSetLogExtraInfo()) {
        try {
          val extraObj = JSONObject.fromObject(billingExtensions.logExtraInfo)
          if (extraObj.containsKey("ctr") && StringUtils.isNotBlank(billingExtensions.getExpose()) && billingExtensions.getExpose.toLong > 0) {
            columnMap.put("predict-ctr", extraObj.getDouble("ctr").toString)
          }
        } catch {
          case ex: Exception => {
            logger.error("get predict-ctr failed exception: ", ex)
          }
          // ignore
        }
      }

      columnMap
    }
  }

//  def main(args: Array[String]) {
//    logger.info("properties starting...")
//    val properties = new Properties()
//    val reader = Files.newReader(new File(args(0)), Charsets.UTF_8)
//    properties.load(reader)
//    reader.close()
//    logger.info("properties end")
//
//    val dimensions = Array("adid", "gender", "province", "age", "mediaType", "dsp", "platform", "city", "education",
//      "incomeLevel", "tagId", "billingType", "experimentId", "reqType", "refType", "templateId", "sessionId", "pageId", "posId",
//      "expAppId", "expLayer1", "expLayer2", "expLayer3", "expLayer4", "expLayer5", "expLayer6", "expLayer7", "expLayer8", "uaId",
//      "upId", "mediaPackage", "placementId", "imei", "refPosition", "subAccountId", "subAccountType", "campaignId", "assetId", "dealId", "isH5",
//      "expBotTypeValue", "env", "seq", "KafkaTopic", "webResVersion", "app", "appId", "expLayer9", "expLayer10", "expLayer11", "expLayer12",
//      "expLayer13", "expLayer14", "expLayer15", "expLayer16", "expLayer17", "expLayer18", "expLayer19", "expLayer20", "expLayer21", "expLayer22",
//      "expLayer23", "expLayer24", "expLayer25", "expLayer26", "expLayer27", "expLayer28", "expLayer29", "expLayer30", "expLayer31")
//
//    val aggregators: Array[AggregatorFactory] = Array(
//      new CountAggregatorFactory("count"),
//      new LongSumAggregatorFactory("fee", "fee"),
//      new LongSumAggregatorFactory("expose", "expose"),
//      new LongSumAggregatorFactory("click", "click"),
//      new LongSumAggregatorFactory("startDownload", "startDownload"),
//
//      new LongSumAggregatorFactory("query", "query"),
//      new LongSumAggregatorFactory("queryFailed", "queryFailed"),
//      new LongSumAggregatorFactory("loaded", "loaded"),
//      new LongSumAggregatorFactory("endDownload", "endDownload"),
//      new LongSumAggregatorFactory("startInstall", "startInstall"),
//      new LongSumAggregatorFactory("endInstall", "endInstall"),
//      new LongSumAggregatorFactory("trueView", "trueView"),
//      new DoubleSumAggregatorFactory("predict-ctr", "predict-ctr")
//    )
//
//    logger.info("TranquilityService start...")
//    val service = new TranquilityService(properties, dimensions, aggregators)
//    logger.info("TranquilityService end")
//
//    var retryCount = 4
//    var success = false
//
//    while (!success && retryCount > 0) {
//      try {
//        logger.info("AdBillingExtensionService main starting...")
//        service.run()
//        success = true
//        logger.info("AdBillingExtensionService main finished...")
//      } catch {
//        case ex: Exception => {
//          retryCount -= 1
//          println(ex.printStackTrace())
//          logger.error("AdBillingExtensionService run exception: ", ex)
//        }
//      }
//    }
//  }
}
