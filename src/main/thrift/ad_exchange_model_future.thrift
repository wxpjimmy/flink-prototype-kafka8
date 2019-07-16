namespace java com.jimmy.ad.exchange.thrift.model.future
namespace py exchange.thrift.model.future

struct Segment{
    1:optional string id; //数据提供商确定的Segment ID
    2:optional string name; //数据提供商确定的名称
    3:optional string value; //数据
}

struct Data{
    1:optional string id; //数据提供商的ID，由交易平台设定
    2:optional string name; //数据提供商的名称
    3:optional list<Segment> segment; //Segment对象列表，Segment代表用户数据
}

struct Geo{
    1:optional double lat; //维度 -90.0 --- +90.0，负值代表南
    2:optional double lon; //经度　-180.0 --- +180.0，负值代表西
    3:optional i32 type; //定位数据的来源
    4:optional string country; //国家编码； ISO-3166-1-alpha-3编码
    5:optional string region; //标识地区；比如华东、华北等等，和openrtb的定义不同
    6:optional string city; //城市
}

struct User{
    1:optional string id; //交易平台定义的用户ID
    2:optional i32 yob; //用户出生年份
    3:optional string gender; //M = male　F = femail　O = unkown
    4:optional string keywords; //用户兴趣或者关键词，comma分割
    5:optional Geo geo; //用户的家庭位置信息
    6:optional list<Data> data; //额外的用户数据
}


struct Deal{
    1:required string id; //直接交易的唯一标识符
    2:optional double bidfloor; //底价
    3:optional i32 at; //竞价类型 1 = first price  2 = second price  其他类型需要额外指定
    4:optional list<string> wseat; //竞价seat白名单，Seat ID 需要平台和竞价方提前约定
    5:optional list<string> wadomain; //广告主domain的白名单
}

struct Pmp{
    1:optional i32 private_auction; //0 = 接受所有seats竞价  1＝只接受deals中指定seats的竞价
    2:optional list<Deal> deals; //Deal 对象列表，用于指定私有的竞价
}



