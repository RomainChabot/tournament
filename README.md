# Tournament application

## Prerequisite

- JDK 11
- NodeJS v14.15.5

## Application configuration

Edit following properties in `application.conf` file

- `ktor.deployment.port`: backend application port
- `ktor.mongodb.url`: MongoDB connection URL

Edit following properties in `src/main/angular/tournament-webapp/src/proxy.conf.json`

- `"target": "http://localhost:8181"` backend application url

## Application startup

### Ktor app

Run `./gradlew run --args="-config=application.conf"`

### Angular app

Go to `src/main/angular/tournament-webapp/`

- Run `npm install`
- Run `ng serve`
