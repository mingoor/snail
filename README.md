<h1 align="center">Snail（蜗牛）</h1>

<p align="center">
基于Java/JavaFX的下载工具，支持下载协议：BT（BitTorrent）、FTP、HTTP。
</p>

<p align="center">
	<a target="_blank" href="https://www.acgist.com">
		<img alt="Author" src="https://img.shields.io/badge/Author-acgist-red.svg?style=flat-square" />
	</a>
	<a target="_blank" href="https://gitee.com/acgist/snail">
		<img alt="Version" src="https://img.shields.io/badge/Version-1.2.0-blue.svg?style=flat-square" />
	</a>
	<a target="_blank" href="https://gitee.com/acgist/snail/releases/v1.1.1">
		<img alt="Release" src="https://img.shields.io/badge/Release-1.1.1-blueviolet.svg?style=flat-square" />
	</a>
	<a target="_blank" href="https://openjdk.java.net/">
		<img alt="Java" src="https://img.shields.io/badge/Java-11-yellow.svg?style=flat-square" />
	</a>
	<a target="_blank" href="https://openjfx.io/">
		<img alt="JavaFX" src="https://img.shields.io/badge/JavaFX-11-green.svg?style=flat-square" />
	</a>
	<a target="_blank" href="https://www.bittorrent.org/beps/bep_0000.html">
		<img alt="BitTorrent" src="https://img.shields.io/badge/BitTorrent-BEP-orange.svg?style=flat-square" />
	</a>
	<img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/acgist/snail?color=crimson&style=flat-square" />
	<img alt="GitHub" src="https://img.shields.io/github/license/acgist/snail?style=flat-square" />
</p>

----

## 使用

#### 编译

```bash
# 编译系统和运行系统不一致时，请修改pom.xml -> os.name属性=目标系统并修改JavaFX依赖，编译完成后可以删除lib目录中编译系统的JavaFX依赖。

# Maven
mvn clean package -P release -D skipTests

# Linux
./builder/build.sh

# Windows
./builder/build.bat
```

#### 启动

```bash
# Linux
# 脚本
./startup.sh
# 命令
java -server -Xms128m -Xmx256m -jar snail-{version}.jar

# Windows
# 脚本
./startup.bat
# 命令
javaw -server -Xms128m -Xmx256m -jar snail-{version}.jar
# 启动器
SnailLauncher.exe
```

> FTP、HTTP下载大文件时，建议优化JVM参数：`-XX:NewRatio=2 -XX:SurvivorRatio=2`

#### Maven

```
<dependency>
    <groupId>com.acgist</groupId>
    <artifactId>snail</artifactId>
    <version>{release.version}</version>
</dependency>
```

## 进度

|功能|进度|
|:--|:--:|
|BT|○|
|FTP|√|
|HTTP|√|

#### BT进度

|BEP|进度|
|:--|:--:|
|Final and Active Process BEPs||
|[The BitTorrent Protocol Specification](http://www.bittorrent.org/beps/bep_0003.html)|√|
|[Known Number Allocations](http://www.bittorrent.org/beps/bep_0004.html)|√|
|[Peer ID Conventions](http://www.bittorrent.org/beps/bep_0020.html)|√|
|Accepted BEPs||
|[DHT Protocol](http://www.bittorrent.org/beps/bep_0005.html)|√|
|[Fast Extension](http://www.bittorrent.org/beps/bep_0006.html)|√|
|[Extension for Peers to Send Metadata Files](http://www.bittorrent.org/beps/bep_0009.html)|√|
|[Extension Protocol](http://www.bittorrent.org/beps/bep_0010.html)|√|
|[Peer Exchange (PEX)](http://www.bittorrent.org/beps/bep_0011.html)|√|
|[Multitracker Metadata Extension](http://www.bittorrent.org/beps/bep_0012.html)|√|
|[Local Service Discovery](http://www.bittorrent.org/beps/bep_0014.html)|√|
|[UDP Tracker Protocol for BitTorrent](http://www.bittorrent.org/beps/bep_0015.html)|√|
|[HTTP/FTP Seeding (GetRight-style)](http://www.bittorrent.org/beps/bep_0019.html)|?|
|[Tracker Returns Compact Peer Lists](http://www.bittorrent.org/beps/bep_0023.html)|√|
|[Private Torrents](http://www.bittorrent.org/beps/bep_0027.html)|○|
|[uTorrent Transport Protocol](http://www.bittorrent.org/beps/bep_0029.html)|√|
|[Holepunch Extension](http://www.bittorrent.org/beps/bep_0055.html)|√|
|Draft BEPs||
|[IPv6 Tracker Extension](http://www.bittorrent.org/beps/bep_0007.html)|×|
|[Superseeding](http://www.bittorrent.org/beps/bep_0016.html)|?|
|[HTTP Seeding (Hoffman-style)](http://www.bittorrent.org/beps/bep_0017.html)|?|
|[Extension for partial seeds](http://www.bittorrent.org/beps/bep_0021.html)|○|
|[Merkle tree torrent extension](http://www.bittorrent.org/beps/bep_0030.html)|?|
|[Failure Retry Extension](http://www.bittorrent.org/beps/bep_0031.html)|?|
|[IPv6 extension for DHT](http://www.bittorrent.org/beps/bep_0032.html)|×|
|[DHT scrape](http://www.bittorrent.org/beps/bep_0033.html)|?|
|[Torrent Signing](http://www.bittorrent.org/beps/bep_0035.html)|?|
|[UDP Tracker Protocol Extensions](http://www.bittorrent.org/beps/bep_0041.html)|?|
|[DHT Security Extension](http://www.bittorrent.org/beps/bep_0042.html)|?|
|[Read-only DHT Nodes](http://www.bittorrent.org/beps/bep_0043.html)|?|
|Storing arbitrary data in the DHT](http://www.bittorrent.org/beps/bep_0044.html)|?|
|Multiple-address operation for the BitTorrent DHT](http://www.bittorrent.org/beps/bep_0045.html)|?|
|Updating Torrents Via DHT Mutable Items](http://www.bittorrent.org/beps/bep_0046.html)|?|
|Padding files and extended file attributes](http://www.bittorrent.org/beps/bep_0047.html)|×|
|[Tracker Protocol Extension: Scrape](http://www.bittorrent.org/beps/bep_0048.html)|×|
|[Publish/Subscribe Protocol](http://www.bittorrent.org/beps/bep_0050.html)|?|
|[DHT Infohash Indexing](http://www.bittorrent.org/beps/bep_0051.html)|?|
|[The BitTorrent Protocol Specification v2](http://www.bittorrent.org/beps/bep_0052.html)|?|
|[Magnet URI extension - Select specific file indices for download](http://www.bittorrent.org/beps/bep_0053.html)|?|
|[The lt_donthave extension](http://www.bittorrent.org/beps/bep_0054.html)|?|
|Other Protocol||
|IPv6|○|
|[STUN](https://www.rfc-editor.org/rfc/rfc5389.txt)|√|
|[UPnP](http://upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.0.pdf)|√|
|upload_only|√|
|[Message Stream Encryption](https://wiki.vuze.com/w/Message_Stream_Encryption)|√|

*√=完成、○-进行中、×-未开始、?-待定*

## License

|软件|License|许可|
|:--|:--:|:--|
|[h2](http://www.h2database.com)|[License](http://www.h2database.com/html/license.html)|MPL 2.0/EPL 1.0|
|[slf4j](https://www.slf4j.org)|[License](https://www.slf4j.org/license.html)|MIT|
|[logback](https://logback.qos.ch)|[License](https://logback.qos.ch/license.html)|LGPL 2.1/EPL 1.0|
|[JavaFX](https://wiki.openjdk.java.net/display/OpenJFX/Main)|[License](http://openjdk.java.net/legal/gplv2+ce.html)|GPL 2.0|
|[Snail](https://gitee.com/acgist/snail)|[License](https://gitee.com/acgist/snail/blob/master/LICENSE)|BSD 3-clause|

## 其他

###### GUI开发工具

JavaFX Scene Builder

###### GIT

GITEE：[https://gitee.com/acgist/snail](https://gitee.com/acgist/snail)  
GITHUB（不活跃）：[https://github.com/acgist/snail](https://github.com/acgist/snail)

###### 帮助

[https://gitee.com/acgist/snail/wikis](https://gitee.com/acgist/snail/wikis)

###### 界面

![主界面](https://static.acgist.com/demo/snail/snail.png "主界面")

###### 更多

[https://www.acgist.com/snail](https://www.acgist.com/snail)

## 评论

首先感谢各位的支持，但是评论区请不要贴异常信息，请提交[issues](https://gitee.com/acgist/snail/issues)。

谢谢！