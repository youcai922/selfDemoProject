本地采用go-cqhttp+SpringBoot实现

go-cqhttp官网地址：https://docs.go-cqhttp.org/

参考https://blog.csdn.net/qq_45738810/article/details/122075899

安装调试之后需要开启servers.http.post.-url和servers.http.post.servret

```
# 连接服务列表
servers:
  # 添加方式，同一连接方式可添加多个，具体配置说明请查看文档
  #- http: # http 通信
  #- ws:   # 正向 Websocket
  #- ws-reverse: # 反向 Websocketa
  #- pprof: #性能分析服务器

  - http: # HTTP 通信设置
      address: 0.0.0.0:5700 # HTTP监听地址
      timeout: 5      # 反向 HTTP 超时时间, 单位秒，<5 时将被忽略
      long-polling:   # 长轮询拓展
        enabled: false       # 是否开启
        max-queue-size: 2000 # 消息队列大小，0 表示不限制队列大小，谨慎使用
      middlewares:
        <<: *default # 引用默认中间件
      post:           # 反向HTTP POST地址列表
       - url: 'http://127.0.0.1:8888/'                # 地址
```