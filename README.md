# logging-previewer

<p align="center">
	<a target="_blank" href="https://github.com/mengweijin/logging-preview-spring-boot-starter/blob/master/LICENSE">
		<img src="https://img.shields.io/badge/license-Apache2.0-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href="https://gitee.com/mengweijin/logging-previewer/stargazers">
		<img src="https://gitee.com/mengweijin/logging-previewer/badge/star.svg?theme=dark" alt='gitee star'/>
	</a>
	<a target="_blank" href='https://github.com/mengweijin/logging-previewer'>
		<img src="https://img.shields.io/github/stars/mengweijin/logging-previewer.svg?style=social" alt="github star"/>
	</a>
</p>

## Description
用浏览器在线实时刷新预览和下载 SpringBoot 项目的日志，不必再登陆到 Linux 服务器上去查看了。

### 使用
在这里下载 fat jar：[logging-previewer-{version}-alpha.jar](https://gitee.com/mengweijin/logging-previewer/releases/)
并上传到要查看日志的应用部署的服务器上。

```bash
# 启动
java -jar logging-previewer-{version}-alpha.jar

# 然后浏览器访问：http://IP:9999
```




### 预览
![image](docs/image/index.png)

![image](docs/image/preview.png)

![image](docs/image/download.png)
