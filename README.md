# fortaWatcher
## 有问题或是改进建议欢迎提issue
修改application.yml配置文件就可以
以下位示例
```
watcher:
  botToken: "5361343175:AAGoLxFzG1m0PpqTDmV83mebE"
  chatId: "-875"
  addressList:
    - "0x0c1AE552C4"
    - "0x027b2B9b51"
    - "0x66b2DbeD1e"
    - "0xa4f120f448"
```

# 部署
使用maven工具打包将jar
复制/opt/javaapp/fortaWatcherExecutor目录下
```
1、docker-compose.yml
2、.env
3、fortaWather-0.0.1-SNAPSHOT.jar（maven打包出来的）
```
直接使用命令
启动
```
docker compose up -d 
```
停止
```
docker compose down
```
其他更多命令参考官网

# 赞助
如果有帮助感谢打赏，给我一键三连也行。
```
eth address: 0x1B43df8d75ed5Bf15a28bbd92fFB5c49fd9D8Ed0
```