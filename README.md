##RPC远程调用框架
rpc远程调用通用框架，提供一个端口多个服务同时高并发部署方案，同时提供安全，接口访问频率基础过滤器支持。
```tps
client: rpc-client.jar -h10.120.47.41 -p44444 -t300000 -th200 -c25000 -s1000 
server: rpc-server.jar -h0.0.0.0 -p44444 -th200 -t600000
average benchmark RpcServerTest.java clientsSize:29325 time:300002 calls:151307 tps:504
```

###服务端

>添加远程服务

添加一个远程服务
```java
String host = "127.0.0.1";
int port = 4332;
AbstractRpcServer server = new SimpleRpcServer();
server.setHost(host);
server.setPort(port);
//TODO注册远程服务
//TODO注册过滤器
//注册完远程服务和过滤器之后再启动服务
server.startService();
```

>注册远程服务

简单注册多个远程服务，接口自定义
```java
//实例化一个实现接口HelloRpcService的对象
HelloRpcService helloRpcServiceImpl = new HelloRpcServiceImpl();
//注册为远程服务
server.register(HelloRpcService.class, helloRpcServiceImpl);
//注册为远程服务添加版本支持,级别为service级别
//server.register(HelloRpcService.class, helloRpcServiceImpl,"v1.1");
```

>添加过滤器

添加ip端口 log和安全检查过滤器,过滤器实现RpcFilter接口即可

```java
server.addRpcFilter(new MyTestRpcFilter());
server.addRpcFilter(new RpcLoginCheckFilter());
```

###客户端

>注册远程服务

初始化并启动
```java
String host = "127.0.0.1";
int port = 4332;
client = new SimpleRpcClient();
client.setHost(host);
client.setPort(port);
//启动服务
client.startService();
```

>添加远程接口，得到实现代理对象

添加远程调用bean

```java
LoginRpcService loginService = client.register(LoginRpcService.class);
HelloRpcService helloRpcService = client.register(HelloRpcService.class);
//选择版本，说明：版本是远程服务service版本，若服务端未找到，不会执行，会产生remote Exception
//HelloRpcService helloRpcService = client.register(HelloRpcService.class,"v1.1");
```

>调用远程服务

业务逻辑

```java
helloRpcService.sayHello("this is HelloRpcService",564);
loginService.login("linda", "123456");
String hello = helloRpcService.getHello();
```