## 简单插件化使用说明
新建一个module 创建一个Test类 编辑之后生成字节码文件 在build/intermediates/javac/debug/classes/包名/Test.class
### 通过dx命令生成dex文件
![图片](D:\downloaddemo\plugin\com\atu\simpleplugindemo\1612407957.jpg)
这种情况需要把Test.java 和Test.class文件放在一块 重新执行命令

如果没有dx命令 把C:\Users\tutuzai\AppData\Local\Android\Sdk\build-tools\30.0.2配置到环境环境变量即可

之后需要把output.dex放进sdcard中 执行adb push output.dex \sdcard\
会出现 ![](D:\downloaddemo\plugin\error.jpg) 没有权限
尝试了root 没有解决，走了一个曲线保全之路  通过文件读写 写入到sacard

之后通过反射 类加载器  把dex文件和apk的classes.dex合并到一块 放进宿主dex 这样app启动的时候就能从新的dexEliments去取