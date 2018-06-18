## 一个简单的http网络请求封装
适用于Api响应json数据结构固定，类似如下这样的情况：
```
{
    "status": "000",
    "message": "请求成功",
    "data": {
        "country": "中国",
        "city": "上海"
    }
}
```
可根据自身情况修改Response中常量配置

## 主要解决以下问题
- 避免每次请求写如下代码
```
apiService.subscribeOn(Schedulers.io())
	.observeOn(AndroidSchedulers.mainThread())
```
- 只需要在api方法简单指定返回类型Observable<T>中T的类型为data的实际类型即可自动提取data字段
- 错误统一处理
- Loading处理