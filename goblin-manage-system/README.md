# 服务管理
http://blog.didispace.com/spring-boot-run-backend/
## 以脚本管理
+ start.sh 
启动服务
+ stop.sh
关闭服务 
+ restart.sh
重启服务

## 以系统服务进行管理

sudo ln -s /xxx/xxx/goblin-manage-system-webapp.jar /etc/init.d/goblin-manage-system-webapp
	
/etc/init.d/goblin-manage-system-webapp start|stop|restart
