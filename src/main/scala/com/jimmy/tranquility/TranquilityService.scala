package com.jimmy.tranquility

import java.util.Properties

import com.metamx.common.Granularity
import com.metamx.tranquility.beam.ClusteredBeamTuning
import com.metamx.tranquility.druid.{DruidBeams, DruidLocation, DruidRollup, SpecificDruidDimensions}
import com.twitter.finagle.Service
import com.twitter.util.Await
import io.druid.granularity.QueryGranularity
import io.druid.query.aggregation.AggregatorFactory
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.log4j.Logger
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, Period}

import scala.collection.mutable

/**
 * 配置文件参见service.example.properties
 *
 * 指定维度：
 * val dimensions = Vector("d1", "d2")
 *
 * 设置指标：
 * val aggregators = Seq(
 * new CountAggregatorFactory("cnt"),
 * new LongSumAggregatorFactory("f1", "f1"),
 * new LongSumAggregatorFactory("f2", "f2")
 * )
 */
class TranquilityService(
  properties: Properties,
  dimensions: IndexedSeq[String],
  aggregators: Seq[AggregatorFactory]) {

  val logger = Logger.getLogger(this.getClass)

  val config = new Config(properties)

  val timestamper = (eventMap: Map[String, Any]) => new DateTime(eventMap(config.jobTimestamp))

  val (druidService, curatorFramework) = buildService()

  def buildService(): (Service[Seq[Map[String, Any]], Int], CuratorFramework) = {
    // Tranquility needs to be able to extract timestamps from your object type (in this case, Map<String, Object>).
    val curator = CuratorFrameworkFactory
      .builder()
      .connectString(config.druidZookeeper)
      .retryPolicy(new ExponentialBackoffRetry(1000, 20, 30000))
      .build()
    curator.start()

    logger.info("start curator")

    val druidService = DruidBeams
      .builder(timestamper)
      .curator(curator)
      .discoveryPath(config.druidDiscoveryCuratorPath)
      .location(DruidLocation(config.druidSelectorsIndexingServiceName, config.druidFirehosePattern, config
      .jobDataSource))
      .rollup(DruidRollup(SpecificDruidDimensions(dimensions), aggregators, QueryGranularity.fromString(config
      .jobQueryGranularity)))
      .tuning(
        ClusteredBeamTuning(
          segmentGranularity = Granularity.valueOf(config.jobSegmentGranularity),
          windowPeriod = new Period(config.jobWindowPeriod),
          partitions = config.jobPartitions,
          replicants = config.jobReplicants
        )
      )
      .buildService()

    logger.info("build druid service")
    (druidService, curator)
  }

  def close(): Unit = {
        Await.result(druidService.close())
        curatorFramework.close()
        logger.info("TranquilityService run finished.")
  }

  val inDayFmt = DateTimeFormat.forPattern("HH:mm:ss")

  def run(): Unit = {
//    val (druidService, curator) = buildService()
//    val consumer = new KafkaConsumer(config.kafkaTopic, config.kafkaGroupId, config.kafkaZookeeper)
//
//    var buffer = mutable.Buffer[Map[String, Any]]()
//    var tmpBuf: mutable.Buffer[Map[String, Any]] = null
//
//    /**
//     * 上一分钟，用来界定上一分钟和下一分钟
//     */
//    var lastMinute: Int = -1
//    /**
//     * 上一分钟处理消息数
//     */
//    var minuteProcessed: Int = 0
//    /**
//     * 上一分钟发送消息数
//     */
//    var minuteSent: Int = 0
//    /**
//     * 上一分钟解析失败的数量
//     */
//    var minuteBlankRecord: Int = 0
//
//    /**
//     * 上一分钟最后一条与服务器时间落后时间，秒
//     */
//    var fallBehind: Long = 0
//
//    /**
//     * 上一次发送时间，在过了间隔batch.sent.interval之后需要立即发送，保证实时性
//     */
//    var lastSent: Long = DateTime.now().getMillis
//
//    for (msg <- consumer.stream) {
//      val date = DateTime.now()
//      val now = date.getMillis
//      try {
//        val events = process(msg.message())
//        if (events != null && events.size > 0) {
//          buffer ++= events
//          minuteProcessed += events.size
//        } else {
//          minuteBlankRecord += 1
//        }
//        if(buffer.length > 0) {
//            fallBehind = now - timestamper(buffer.head).getMillis
//        }
//      } catch {
//        case e: Exception => logger.warn("error in parsing message", e)
//      }
//
//      if (buffer.length > 0 && (now - lastSent > config.jobBatchSentInterval || buffer.length >= config.jobBatchSent)) {
//        tmpBuf = buffer
//        druidService(tmpBuf) onSuccess {
//          sent =>
//            minuteSent += sent //todo: syncronized
//        } onFailure { ex => logger.error("send failed", ex) }
//        buffer = mutable.Buffer[Map[String, Any]]()
//        lastSent = now
//      }
//
//      if (date.getMinuteOfHour != lastMinute) {
//        logger.info(s"[${
//          date.toString(inDayFmt)
//        }] Produced: $minuteProcessed, Sent $minuteSent, Blank: $minuteBlankRecord, Fall behind: $fallBehind")
//        minuteSent = 0
//        minuteProcessed = 0
//        lastMinute = date.getMinuteOfHour
//        minuteBlankRecord = 0
//      }
//    }
//    Await.result(druidService.close())
//    curator.close()
//    logger.info("TranquilityService run finished.")
  }
}
