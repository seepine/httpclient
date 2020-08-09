## httpclient

httpclient封装，线程池、代理、HttpUtil

## 依赖
```xml
<dependency>
    <groupId>com.seepine</groupId>
    <artifactId>httpclient</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用方法

### 1.简单请求
```java
String res = HttpUtil.get("http://127.0.0.1:8080/get");
String res = HttpUtil.post("http://127.0.0.1:8080/post",new HashMap<String,Object>());
String res = HttpUtil.postJson("http://127.0.0.1:8080/post",jsonStr);
String res = HttpUtil.put("http://127.0.0.1:8080/put",new HashMap<String,Object>());
String res = HttpUtil.delete("http://127.0.0.1:8080/delete");
```
### 2.文件下载
简单下载
```java
HttpUtil.download("https://127.0.0.1:8080/156165.mp4"
                    , "E:\\a.mp4"); 
```
监测进度
```java
try {
    HttpUtil.download("https://127.0.0.1:8080/156165.mp4"
            , "E:\\a.mp4", new StreamProgress() {
                private long contentLength = 0;
                @Override
                public void startBefore(long contentLength) {
                    System.out.println("开始下载：" + contentLength);
                    this.contentLength = contentLength;
                }
                @Override
                public void start() {
                    System.out.println("开始");
                }
                @Override
                public void progress(long progressSize) {
                    System.out.println("进度：" + progressSize * 1.0 / contentLength * 100);
                }
                @Override
                public void finish() {
                    System.out.println("下载完成");
                }
            });
} catch (IOException e) {
    e.printStackTrace();
}
```
### 2.自定义请求
```java
Request req = Request.build("POST","http://127.0.0.1:8080/post/param")
                        .json(new HashMap<String,Object>())
                        .header("token","111111111");
Response res = HttpUtil.execute(req);
```