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
#### chatId获取
创建telegram机器人后，获得机器人token，替换{{token}}为对应到机器人，创建一个group，请求下面连接可以获取到chatId，
如果获取不到踢出机器人然后重新加入群再试。 chatId一般为一个副整数
```
https://api.telegram.org/bot{{token}}/getUpdates
```
# 部署
使用maven工具打包
复制下面3个文件到部署服务器目录/opt/javaapp/fortaWatcherExecutor
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