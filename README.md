![plot](https://github.com/bibimchi/whiteplayground.frontend/blob/master/src/asset/favicon.png)

## Development Recipe
* start TransferApplication.java from intellij 
* open browser to http://localhost:8096/swagger-ui.html

## Docker Recipe
* gradle build
* docker build -t imagename .
* docker run --detach --publish 8096:8096 --name containername --env spring.config.name="local.docker" imagegname
* open browser to http://localhost:8096/swagger-ui.html

## Demo
* [here](https://kimchistudio.tech/wp/back/swagger-ui.html)

## Docker image
* [here](https://hub.docker.com/repository/docker/kimchiboy/wp_back)
