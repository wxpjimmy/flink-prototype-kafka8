package com.jimmy.tranquility

import java.util.Properties

/**
 * @author du00
 */
case class Config(
    druidZookeeper: String,
    druidDiscoveryCuratorPath: String,
    druidSelectorsIndexingServiceName: String,
    druidFirehosePattern: String,

    jobDataSource: String,
    jobQueryGranularity: String,
    jobSegmentGranularity: String,
    jobWindowPeriod: String,
    jobTimestamp: String,
    jobPartitions: Int,
    jobReplicants: Int,
    jobBatchSent: Int,
    jobBatchSentInterval: Int,

    kafkaTopic: String,
    kafkaGroupId: String,
    kafkaZookeeper: String) {
  def this(properties: Properties) {
    this(
      //druid basic x4
      properties.getProperty("druid.zookeeper"),
      properties.getProperty("druid.discovery.curator.path", "/prod/discovery"),
      properties.getProperty("druid.selectors.indexing.serviceName"),
      properties.getProperty("druid.firehosePattern", "druid:firehose:%s"),

      // job x 7
      // 数据源
      properties.getProperty("job.data.source"),
      // 查询粒度
      properties.getProperty("job.query.granularity", "MINUTE"),
      // 落地间隔，不建议设置太大，query.granularity < segment.granularity
      properties.getProperty("job.segment.granularity", "DAY"),
      // 窗口，超过窗口时间的记录将会被丢弃
      properties.getProperty("job.window.period", "PT10M"),
      // timestamp字段所在的key
      properties.getProperty("job.timestamp"),
      properties.getProperty("job.partitions", "1").toInt,
      properties.getProperty("job.replicants", "1").toInt,
      // 累积xx条再发送
      properties.getProperty("job.batch.sent", "1000").toInt,
      // 间隔xx毫秒发送一次，与batch.sent二者满足一条即可，会牺牲实时性
      properties.getProperty("job.batch.sent.interval", "5000").toInt,

      //kafka
      properties.getProperty("kafka.topic"),
      properties.getProperty("kafka.group.id"),
      properties.getProperty("kafka.zookeeper")
    )
  }
}
