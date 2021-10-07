##Development Recipe
start TransferApplication.java from intellij 
open browser to http://localhost:8096/swagger-ui.html

##Docker Recipe
gradle build
docker build -t imagename .
docker run --detach --publish 8096:8096 --name containername imagegname
open browser to http://localhost:8096/swagger-ui.html

##demo
[here](https://kimchistudio.tech/wp/back/swagger-ui.html)
##docker image
[here](https://hub.docker.com/repository/docker/kimchiboy/wp_back)