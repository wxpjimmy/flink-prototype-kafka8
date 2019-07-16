namespace java com.jimmy.ad.exchange.thrift.model
namespace py exchange.thrift.model

include "ad_exchange_model_future.thrift"

typedef ad_exchange_model_future.User User

enum DspBiddingType {
    UNKNOWN = 0,
    PRIVATE_DIRECT_BUY = 1,
    PREFERRED_DEALS = 2,
    REAL_TIME_BIDDING = 3,
    RETENTION = 4
}

enum ActionType {
    VIEW=0,   //广告曝光
    CLICK=1,  //用户点击下载按钮
    APP_START_DOWNLOAD=2, //广告提交给下载管理器开始下载
    APP_DOWNLOAD_SUCCESS=3,   //下载成功
    APP_INSTALL_START=4, //开始安装
    APP_INSTALL_START_FROM_NOTIFICATION=5, //从通知栏中开始安装
    APP_INSTALL_SUCCESS=6,    //安装成功
    APP_INSTALL_FAIL=7,    //安装失败
    SHOW_NOTIFICATION=8,
    APP_DOWNLOAD_FAIL=9, //广告下载失败

    REQUEST_FAIL = 12,
    LOAD = 13,
    LOAD_FAIL = 14,
    DISLIKE = 15,

    APP_LAUNCH_START = 16,
    APP_LAUNCH_SUCCESS = 17,
    APP_LAUNCH_FAIL = 18,

    VIDEO_START = 19,
    VIDEO_PAUSE = 20,
    VIDEO_FINISH = 21,
    VIDEO_RESUME = 22,

    CLOSE = 23,

    VIDEO_TIMER = 24, // 定时打点

    DOWNLOAD_SUCCESS = 25,  //广告素材下载成功
    DOWNLOAD_FAIL = 26,  //广告素材下载失败

    SKIP = 27,
    FINISH = 28,

    APP_DOWNLOAD_CANCEL = 29, // 取消下载

    INTERRUPT = 30, // 中断

    NOTIFICATION_RECEIVE = 31, //通知接收
    NOTIFICATION_REMOVE = 32, //通知移除

    VIDEO_LOADING = 33,//加载视频
    VIDEO_END = 34,//视频播放中离开屏幕
    VIDEO_FAIL = 35,//视频缓冲或播放失败
    RESERVE = 36,   // 新闻资讯点击电话拨打按钮
    SDK_RESPONSE = 37, // 客户端决策下的离线广告请求-响应
    VIDEO_Q1 = 38, // 视频在四分之一处打点事件
    VIDEO_Q2 = 39, // 视频在播放到一半处打点事件
    VIDEO_Q3 = 40, // 视频在播放到四分之三处打点事件
    DETAILED_VIEW = 41, //视频详情页打点事件
    EXPAND = 42, //视频点击打点事件
    WIN = 43, // 联盟RTB竞价成功 winnotice
    LEAVE_WEBVIEW = 45,//用户离开webview事件

    RESOURCE_CACHE_MISS = 47, // 预下发素材未命中

    LANDINGPAGE_PRE_DOWNLOAD_SUCCESS = 49, //素材预下载成功
    LANDINGPAGE_PRE_DOWNLOAD_FAIL = 50, //素材预下载失败

    VIDEO_CREATE_VIEW = 51, //视频卡展示事件
    VIDEO_SHOW_TIME = 52, //视频广告展示时间

    APP_LAUNCH_START_DEEPLINK = 53, //deeplink url拉起开始
    APP_LAUNCH_SUCCESS_DEEPLINK = 54, //deeplink url拉起成功
    APP_LAUNCH_FAIL_DEEPLINK = 55, //deeplink url拉起失败

    APP_LAUNCH_START_PACKAGENAME = 56, //包名拉起开始
    APP_LAUNCH_SUCCESS_PACKAGENAME = 57, //包名拉起成功
    APP_LAUNCH_FAIL_PACKAGENAME = 58, //包名拉起失败

    TRUE_VIEW = 59, //真实曝光

    APP_FIRST_LAUNCH_DEEPLINK_SUCCESS = 60,  // 安装完成后，自动拉起deeplink成功
    APP_FIRST_LAUNCH_DEFAULT_SUCCESS = 61,  // 安装完成后，自动拉起应用默认页成功
    APP_FIRST_LAUNCH_DEFAULT_FAIL = 62, // 安装完成后，自动拉起应用默认页失败
    APP_OTHER_LAUNCH_DEEPLINK_SUCCESS = 63,  // 已安装用户，拉起deeplink成功
    APP_OTHER_LAUNCH_DEFAULT_SUCCESS = 64,   // 已安装用户，拉起应用默认页成功
    APP_OTHER_LAUNCH_DEFAULT_FAIL = 65,  // 已安装用户，拉起应用默认页失败

    APP_DOWNLOAD_PAUSE = 66, // APP下载暂停事件
    APP_DOWNLOAD_RESUME = 67, // APP下载恢复事件

    REWARD_SUCCESS = 68, // 激励下载类广告，激励已下发打点

    APP_H5_LAUNCH_START = 69, // 开始打开H5(启动webview)
    APP_H5_LAUNCH_SUCCESS = 70, // H5打开成功
    APP_H5_LAUNCH_FAIL = 71, // H5打开失败（超时，4xx，5xx等错误）

    TYPE_DOWNLOAD_SOURCE_MIXX_MARKET = 72, // 下载途径，应用商店
    TYPE_DOWNLOAD_SOURCE_NATIVE = 73, // 下载途径，使用MSA原生下载
    TYPE_DOWNLOAD_SOURCE_MIMO_SDK_NATIVE = 74, // 下载途径，SDK自带原生下载

    LANDSCAPE_VIEW = 75,  // 横版素材曝光
    LANDSCAPE_CLICK = 76,    // 横版素材点击
    LANDSCAPE_CLOSE = 77,   // 横版素材关闭
    LANDSCAPE_FINISH = 78,   // 横版素材结束

    PORTRAIT_VIEW = 79,   // 竖版素材曝光
    PORTRAIT_CLICK = 80,  // 竖版素材点击
    PORTRAIT_CLOSE = 81,   // 竖版素材关闭
    PORTRAIT_FINISH = 82,  // 竖版素材结束

    APP_ACTIVE = 83, // 商店检测的应用激活
    M3RD_ACTIVE = 84, // 第三方回传的应用激活，海外专用

    AD_OPTION_CLOSE = 85, // 用户在app中设置的关闭广告推送事件
    AD_OPTION_OPEN = 86, // 用户在app中设置的打开广告推送事件
    PREVIOUS_VIEW = 87,  // 导流广告的展现
    PREVIOUS_CLICK = 88, // 导流广告的点击

    END_PAGE_VIEW = 89, // 结束页曝光
    PIC_START = 90,
    PIC_FINISH = 91,

    UNKNOWN = 127

}


struct DirectDeal{
    1:required string id; //直接交易标识 ID；由交易平台和 DSP 提前约定
}

struct Publisher{
    1:optional string id; //交易平台定义的 publisher ID
    2:optional string name; //publisher 名称（可能在publisher 请求中修改）
    3:optional list<string> cat; //publisher 内容类别列表
    4:optional string domain; //publisher 最高层次的domain
}

struct Device{
    1:optional string ua; //Browser user agent
    2:optional string ip; //ipv4地址
    3:optional string ipv6; //ipv6地址
    4:optional i32 devicetype; //设备类型
    5:optional string make; //设备厂商
    6:optional string model; //设备型号
    7:optional string os; //操作系统
    8:optional string osv; //操作系统版本
    9:optional string hwv; //设备硬件版本号
    10:optional i32 h; //屏幕高；像素
    11:optional i32 w; //屏幕宽；像素
    12:optional i32 ppi; //每英寸屏幕大小；像素
    13:optional double pxratio; //物理像素和设备独立像素的比例
    14:optional i32 js; //是否支持javascipt;0 = no ; 1 = yes
    15:optional string flashver; // 浏览器支持的flash版本
    16:optional string language; //浏览器语言;ISO-639-1-alpha-2编码
    17:optional string carrier; //网络服务提供商
    18:optional i32 connectiontype; //网络连接类型
    19:optional string ifa; //由广告主定制的ID
    20:optional string didsha1; //imei hash via sha1
    21:optional string didmd5; //imei hash via md5
    22:optional string dpidsha1; //Android ID hash via sha1
    23:optional string dpidmd5; //Android ID hash via md5
    24:optional string idfasha1; //idfa hash via sha1
    25:optional string idfamd5; //idfa hash via md5
    26:optional string macsha1; //mac地址sha1
    27:optional string macmd5; //mac地址md5
    28:optional string cookie; //cookie ID
    29:optional string dpid; //Android ID
    30:optional string gpid; //google play's AD ID
    31:optional string osVersion; //os Version
    32:optional string mac; // mac地址
}

struct Site{
    1:optional string id; //交易平台定义的site ID
    2:optional string name; //Site 名（可能被流量方起别名
    3:optional string domain; //站点domain
    4:optional list<string> cat; //站点的内容类别列表
    5:optional string page; //当前页面URL
    6:optional string ref; //ref URL
    7:optional string search; //搜索词
    8:optional i32 mobile; //移动优化标识 0 = no ; 1 = yes
    9:optional Publisher publisher; //站点Publisher信息
    10:optional string keywords; //站点关键词描述，以comma隔开
}

struct App{
    1:optional string id; //交易平台设定的app id
    2:optional string name; //应用名称（可能在流量方的请求中重命名）
    3:optional string bundle; //app包名; 在交易平台中唯一
    4:optional string domain; //app对应的domain
    5:optional string storeurl; //应用商店中的URL地址
    6:optional list<string> cat; //app的内容类别列表
    7:optional string ver; //app 版本号
    8:optional i32 paid; //0 = app 免费 ; 1 = app 付费
    9:optional Publisher publisher; //app 对应的publisher信息
    10:optional string keywords; //app 的关键词描述；comma分割
}

struct Splash{
    1:optional i32 w; //广告位的宽度；像素
    2:optional i32 h; //广告位的高度；像素
    3:optional i32 skip; //是否可以跳过；1=yes；0=no
    4:optional i32 duration; //广告持续时间；毫秒；
    5:optional i32 detailpos; //详情图标的位置；待定义
    6:optional list<i32> btype; //拒绝的广告类型；
    7:optional list<i32> battr; //拒绝的创意类型
    8:optional list<string> mines; //MIME 类型支持，选项通常包括 ” image/jpg ”,　”application/x-shockwave-flash”, “ image/gif ”
    9:optional i32 frequencycapping; //是否支持频次控制；交易平台预取广告时的专用字段；1=yes; 0=no
}

struct Video{
    1:optional list<string> mines; //支持播放的广告格式，video/x-ms-wmv, video/x-flv
    2:optional i32 minduration; //视频广告最小时长；秒
    3:optional i32 maxduration; //视频广告最大时长；秒
    4:optional list<i32> protocols; //支持的视频广告协议列表
    5:optional i32 w; //广告位宽度；像素
    6:optional i32 h; //广告位高度；像素
    7:optional double size; //视频大小:MB
    8:optional list<i32> battr; //禁止的创意属性
    9:optional i32 frequencycapping; //是否支持频次控制；交易平台预取广告时的专用字段；1=yes; 0=no
}

struct TextLink{
    1:optional string id;
    2:optional list<string> btype;
    3:optional list<i32> battr;
    4:optional i32 length;
    5:optional i32 size;
}

struct Native{
    1:required string request; //遵循原生广告指定规格的请求信息
    2:optional string id;
    3:optional i32 w;
    4:optional i32 h;
    5:optional string ver; //request 版本
    6:optional list<i32> api; //支持的API框架列表
    7:optional list<i32> battr; //禁止的创意属性
    8:optional string layout_type; // 信息流（嵌入式广告）：12，下载类广告：7 //should remove ?????
}

struct Banner{
    1:optional i32 w; //广告位的宽度；像素
    2:optional i32 h; //广告位的高度；像素
    3:optional string id; //Banner 对象ID；通常从１开始
    4:optional list<i32> btype; //拒绝的广告类型
    5:optional list<i32> battr; //拒绝的创意类型
    6:optional i32 embedding; // 嵌入类型；1=视频贴片广告；0=其他；
    7:optional list<string> mines; //MIME类型支持，选项通常包括 "image/jpg",　
                                   //"application/x-shockwave-flash", "image/gif"
}

struct Imp{
    1:required string id; //曝光标识id；通常从１开始
    2:required string tagid; // 广告位标识 ID ;
    3:optional Banner banner; //Banner 对象；banner广告位，该字段必须存在
    4:optional TextLink textlink; //TextLink 对象；文字链广告位，该字段必须存在
    5:optional Native nativead; //Native 对象；原生广告位，该字段必须存在
    6:optional Video video; //Video 对象；视频广告位，该字段必须存在
    7:optional Splash splash; // Splash 对象；开屏广告位，该字段必须存在
    8:optional i32 instl; //是否全插屏广告，0：不是，1：是
    9:optional i32 admtype; //1:H5 2:Json
    11:optional i32 pos; //广告位置类型
    12:optional double bidfloor; //底价，单位为0.01元/CPM
    13:optional DirectDeal directdeal; //直接交易对象
    14:optional list<string> templates; //可以支持的模板类型。如果为空，表示任何都可以
    15:optional i32 adsCount; //请求的广告数量
    17:optional string ext;
    18:optional string query; //浏览器上查询的字段
}


struct Bid{
    1:required string id; //Bidder定义的ID, 用于日志和跟踪
    2:required string impid; //对应Imp对象的ID属性
    3:required double price; //Bid price as CPM
    4:optional string adid; //广告ID
    5:optional string nurl; //Win notice URL
    6:optional string adm; //广告物料
    7:optional string tagid; // 广告位标识 ID ; 若BidRequest中包含tagid, Response中该字段必填
    8:optional string templateid; // 广告模板 ID ; 若 BidRequest 中包含 template, Response 中该字段必填
    9:optional list<string> adomain; //广告对应的domain
    10:optional string bundle; //如果是应用下载类广告，代表应用的包名
    11:optional string cid; //广告campaign ID
    12:optional string crid; //创意ID
    13:optional list<string> cat; //创意的内容类别列表
    14:optional list<i32> attr; //描述创意属性的列表
    15:optional string dealid; //BidRequest中对应的deal.id  适用于私有市场的直接交易;
    16:optional i32 h; //广告创意的高度
    17:optional i32 w; //广告创意的宽度
    18:optional string landingurl; //广告点击目标 URL ， 用于 adm 的#CLICK_URL#宏，作为广告被点击时的目标地址
    19:optional list<string> impurl; //广告展示监播地址，当 adm 中不包括 DSP 展示监播地址或第三方监播地址时使用
    20:optional list<string> curl; //广告点击监播地址 ， 用户 adm 的#CLICK_URL#宏，作为广告被点击时的监播消息地址，URL 必须支持重定向
    21:optional string extdata; //dsp自定义数据，作为${EXT_DATA} 宏定义
    22:optional double cost; //该广告的最终竞价
    23:optional double winecpm; //最终竞价成功的ecpm，有可能是second price或者first price
    24:optional i32 billingType; // 1:CPM 2:CPC 3:CPD
    25:optional list<string> durl; //广告下载监控地址，已废弃，请使用startdurl,finishdurl,installurl
    26:optional list<string> videostarturl; //视频开始播放监测地址
    27:optional list<string> videostopurl; //视频播放停止监测地址
    28:optional list<string> videoendurl; //视频播放结束检测地址
    29:optional i32 targetType;     //广告的类型：外链，应用下载，视频，游戏
    30:optional list<string> startdurl; //开始下载监测地址
    31:optional list<string> finishdurl; //下载完成监测地址
    32:optional list<string> startinstallurl; //应用开始安装监测地址
    33:optional list<string> finishinstallurl; //应用安装完成监测地址
    34:optional string deeplink;  // deeplink
    35:optional string adv; // 广告主名称
    36:optional string cat1; // 一级行业
    37:optional string cat2; // 二级行业
    38:optional string cat3; // 三级行业
}

struct SeatBid{
    1:required list<Bid> bid; //和impression对应，一个impression对应一个Bid，允许对应多个
    2:optional string seat; //bidder seat ID
    3:optional i32 cm; // 是否需要cookie mapping; 1 = yes ; 0 = no
    4:optional i32 group; // default 0: 0 = 可以单独赢下一个impression; 1 = 以组的形式赢下一次BidRequest中的所有impression
    5:optional string ext;
    20:optional DspBiddingType dspBiddingType; // dsp采取的竞价方式
}


struct BidRequest{
    1:required string id; //bid request 唯一 id，由交易平台生成
    2:required list<Imp> imp; //标识广告位曝光的Imp 对象列表，至少一个
    3:optional Site site; //Site流量方对象，只针对website流量时存在
    4:optional App app; //App流量方对象，只针对App流量时存在
    5:optional Device device; //Device 对象，描述用户的设备信息
    6:optional i32 test; //测试字段，是否涉及收费； 0 = 生产模式；１＝测试模式；
    7:optional i32 at; //竞价类型；1 = first price; 2 = second price plus; 交易平台自定义的竞价类型，需大于500；
    8:optional i32 tmax; //该字段较为稳定，需线下提前沟通好；毫秒
    9:optional list<string> bcat; //广告行业黑名单
    10:optional list<string> badv; //广告主黑名单，根据域名标注广告主
    11:optional string ext;
    12:optional User user; //请求的用户信息
}

struct BidResponse{
    1:required string id; //对应的BidRequest中定义的ID
    2:optional list<SeatBid> seatbid; //seatbid对象列表
    3:optional string bidid; //Bidder定义的响应ID，可用于日志和效果追踪
    4:optional i32 nbr; //不竞价的理由
}

enum LogEventType{
    VIEW = 1,
    CLICK = 2,
    BIDDING = 3
}

struct BidEffectLog{
    1:required string triggerID; // 请求中的triggerID 或者认为是　BidRequest中的ID
    2:required LogEventType eventType; // 日志上报的类型　点击还是曝光
    3:optional string didmd5; //　
}

struct CacheEffectLog{
    1:required string triggerID; // 请求中的triggerID 或者认为是　BidRequest中的ID
    2:optional string didmd5;
    3:optional list<string> viewUrls; //　曝光监督列表
    4:optional list<string> clickUrls; //　点击监督列表
}

struct BidUnit{
   1:required string tagId;
   2:required string dspName;
   3:optional string dealId;
   4:optional double bidPrice; //竞价的出价
   6:optional string experimentId;
}

struct BidRequestFailInfo {
    1:optional list<string> tagid;
    2:optional string dsp;
    3:optional string errorMsg;
    4:optional string exceptionSimpleName;
    5:optional list<BidUnit> impInfos;
}

struct AdFilterInfo{
    1: optional string dspName;
    2: optional Bid bid;
    3: optional string filterReason;//过滤类型，目前有关键词过滤和ctr过滤
    4: optional string filterkw;//关键词过滤的关键词，ctr过滤不需要设置
}

struct AdFilterList{
    1:optional list<AdFilterInfo> filteredAds;
}

struct BidUnitFailInfo {
    1: required BidUnit bidUnit;
    2: required string bidFailReason;
    3: optional i64 maxEcpm;    // 最高出价
    4: optional i64 winEcpm;    // 最总价格，二价或一价
    5: optional i64 ecpm;       // 出价
}

struct BidFailInfo{
    1: optional string tagId;
    2: optional map<string,string> dspNameToFailReason;//dsp竞价失败原因 旧
    3: optional list<BidUnitFailInfo> bidUnitFailReasons;//dsp竞价失败原因 新
}

struct BidFailList{
    1: optional list<BidFailInfo> failedBiddings;
}

//struct RequestContext{
//    1: optional UniformRequest request;
//    2: optional string dspName;
//    3: optional BidRequestFailInfo failInfo;
//    4: optional list<AdFilterInfo> filteredAds;
//    5: optional string debugOnlineInfo;
//}

//struct MaxDebugInfo{
//    1: optional list<RequestContext> requestContext; //请求信息追踪
//    2: optional list<BidFailInfo> failedBiddings; //竞价失败信息
//}

enum KafkaMsgType {
    BIDDING = 1,
    RAWEXPOSE = 2,
    PUREEXPOSE = 3
}

struct DSPRequestInfo {
    1:optional string dspName;

    100:optional string ext;
}

struct DSPResponseInfo {
    1:optional string dspName;
    2:optional double minECPM;
    3:optional double maxECPM;
    4:optional double avgECPM;
    5:optional double sumECPM;
    6:optional DspBiddingType dspBillingType;

    100:optional string ext;
}

struct AdDetail {
    1:optional string adId;

    50:optional i16 isWinner;
    55:optional double winecpm; //DSP参与竞价的ecpm
    56:optional double cost; //最终竞价成功的ecpm，有可能是second price或者first price
    60:optional string experimentId; //实验ID
}

struct DSPResponseStat {
    1:required string dspName;
    2:optional DspBiddingType dspBT;
    3:optional string city;
    4:optional string dealId;

    50:optional list<AdDetail> adDetails;
    51:optional double maxECPM;
    52:optional double avgECPM;
    53:optional double sumECPM;
    54:optional double minECPM;
    55:optional map<string,string> ext;
}

struct NoAdResponse {
    1:optional string dspName; //是否存在带有返还比的DSP，如果没有，该值为空
    2:optional i32 count; //没有广告返回，该值为1
}

struct TagIdResponse {
    1:required string tagId;
    2:optional bool isPDB;

    10:required list<DSPResponseStat> dspResponseStats;
    11:optional NoAdResponse noAdResponse;
}

struct KafkaMsg{
    1:required KafkaMsgType msgType;
    2:required i64 dateTime; // ISO format

    20:optional string triggerId;
    21:optional LogEventType logEventTye; // only used for expose part
    22:optional string dspName;
    23:optional string tagId;
    24:optional bool isPDB;
    25:optional string city;
    26:optional string dealId;
    27:optional i32 billingType; //竞价类型
    28:optional i64 adPositionId; //排期中用到的广告位信息
    29:optional string targetId; // max 定向id
    30:optional i64 uploadTimestamp; // 服务端打点时间戳

    39:optional string adid;
    40:optional double winECPM; // only used for bidding part
    41:optional string winDSP; // only used for bidding part
    42:optional double cost; //only used for expose part
    43:optional list<DSPResponseInfo> dspResponses; // only used for bidding part
    44:optional list<TagIdResponse> tagIdResponses; // only used for bidding part

    100:optional map<string,string> extMap;
    101:optional string ext;
}

// Middletiers Billing/Action log flat struct for druid.io ingestion
struct BillingExtensions{
    1:optional string timestamp;
    // ad dims
    2:optional string adid;
    // target dims
    3:optional string gender;
    4:optional string province;
    5:optional string age;
    6:optional string mediaType;
    // measures
    7:optional string fee;
    8:optional string expose;   //view
    9:optional string click;
    10:optional string startDownload;
    // traffic dims
    11:optional string dsp;
    12:optional string platform;
    // target dims
    13:optional string city;
    14:optional string education;
    15:optional string incomeLevel;
    // traffic dims
    16:optional string tagId;   //广告位
    17:optional string billingType;
    18:optional string experimentId;
    // from here are new adds
    19:optional string reqType;		// 新闻资讯请求类型
    20:optional string refType;		// 信息流广告刷新类型
    21:optional string templateId;	// 模板
    // pos dims
    22:optional string sessionId;	// session id
    23:optional string pageId;		// 页面id
    24:optional string posId;		// 广告位 id
    // exp dims
    30:optional string expAppId;	// 实验APP id
    31:optional string expLayer1;	// 实验层级1
    32:optional string expLayer2;
    33:optional string expLayer3;
    34:optional string expLayer4;
    35:optional string expLayer5;
    36:optional string expLayer6;
    37:optional string expLayer7;
    38:optional string expLayer8;
    // new measures for Yidianzixun
    50:optional i32 query;			// 广告请求
    51:optional i32 queryFailed;	// 请求状态：0成功、1失败
    52:optional i32 loaded;			// 广告加载
    53:optional i32 endDownload;	// 下载成功
    54:optional i32 startInstall;		// 开始安装
    55:optional i32 endInstall;		// 安装成功
    //union ad dims
    56:optional string uaId;
    57:optional string upId;
    58:optional string mediaPackage;
    59:optional string placementId;
    //addition dims
    60:optional string imei;
    61:optional string refPosition;
    62:optional string subAccountId;
    63:optional string subAccountType;
    64:optional string campaignId;
    65:optional i64 assetId;
    //addition event
    66:optional i32 rawExpose;
    67:optional i32 rawClick;
    //exteranl dsp dims
    80:optional string dealId;
    //union ad dims
    81:optional string devId;
    82:optional string unionAccessMode; //联盟接入方式 sdk=0, api=1, rtb=2, sysSplash=3
    //app_store ad dims
    83:optional string isH5;
    84:optional string adActionLog; //已废弃
    85:optional string logExtraInfo; //透传extraInfo
    86:optional i32 expBotTypeValue;
    87:optional string triggerId;
    88:optional i32 botType;
    89:optional i64 botTypeValue;
    90:optional string timeZone;   //广告投放定向时区
    91:optional string adUserId;   //统一的用户标志，国内用imei, 国外用gaid
    92:optional string gaid;       //google advertiser id 国外android通用
    93:optional bool isInter;      //是否海外流量
    94:optional i32 trueView; //有效播放曝光
    95:optional map<i32,i32> extraExpLayer; //扩展的expLayer
	96:optional i32 actionType; //打点事件类型
	97:optional string appInfo;     // 应用信息 appId-appName
}

struct AdMateral{
    1: optional i64 adId; // adId
    2: optional string adidString; //外部DSP很多用的是非整数来表示广告id，定义成string，将来也方便扩展
    3: optional string packageName;     //
    4: optional string imgUrl;      // 广告创意的图片url,如果对应的是开屏广告，要求背景图地址，图片小于200K
                                          // 应用商店banner广告的图片地址可以使用这个字段，存的是CDN文件的path
    5: optional string title;       // 广告创意的title
    6: optional string summary;     // 广告创意的描述
    7: optional string landingPageUrl;  //广告创意点击后跳转的url
    8: optional string actionUrl;    //广告的下载url,如果对应的是开屏广告，要求是点击链接地址，可缺省
    9: optional i32 template; // 模板类型，
                                        // 1代表信息流应用下载类广告模板一（大图），
                                        // 2代表信息流应用下载类广告模板二（小图），
                                        // 3代表信息流品牌类广告模板一
                                        // 4代表信息流品牌类广告模板二
                                        // 5代表内文页应用下载类展示广告
                                        // 6代表新闻资讯的开屏广告
                                        // 6代表新闻资讯的开屏广告
                                        // 7代表开屏广告，仅曝光展示的广告
                                        // 8代表开屏广告，有下载的广告
                                        // 10代表信息流应用大图下载广告模板
                                        // 11代表浏览器卡片类条幅广告
                                        // 12代表浏览器搜索框提示app下载类广告
                                        // 20代表新的信息流应用大图下载广告模板，下载按钮在上
                                        // 21代表新的信息流应用大图下载广告模板，下载按钮在下
                                        // 30代表信息流应用组图下载广告模板，下载按钮在上
                                        // 31代表信息流应用组图下载广告模板，下载按钮在下
                                        // 40代表信息流品牌类广告组图模板
    10: optional i32 duration; // 开屏广告的闪屏维持时间，单位毫秒 miliseconds
    11: optional i64 appId;  // 如果是应用商店广告，该字段用于保存app id
    12: optional string iconUrl; // 大图应用下载类广告的应用图标
    13: optional string categoryName; // app应用的level1CategoryName
    14: optional i32 billingType; // 计费类型
    15: optional i64 price; // bid
    16: optional list<string> multiImgUrls; //为信息流组图模板提供的Img Url list
    17: optional string source;  //标识一点资讯的广告来源，品牌广告是品牌名称，应用下载广告是应用名称
    18: optional string videoUrl; //一点资讯视频广告视频播放地址
    19: optional string dspname; //DSP名称
    20: optional string tagId; //应该保持和ClientInfo传进来的一致
    21: optional string adTemplateId; // 广告模板ID
    22: optional list<string> imgUrls;  //图片列表
    23: optional i32 targetType;     //广告的类型：外链，应用下载，视频，游戏
    30: optional string ext; //存储额外信息，JSON string
}

struct AdMateralInfo{
    1: optional list<AdMateral> adMaterals;  //列表
}

exception EffectLogException{
    1:required string message;
}

exception BidRequestException{
    1:required string message;
}

exception NoBidException{
    1:required string message;
}
