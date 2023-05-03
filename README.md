## Workshop 37

## Server side
```
set MYSQL_DB_URL=jdbc:mysql://localhost:3306/blobdb
set MYSQL_DB_USERNAME=root
set MYSQL_DB_PASSWORD=zxc123456

set DO_STORAGE_KEY=DO00THKGPAKMDM7CAKYW
set DO_STORAGE_SECRETKEY=uEMURNwjb11ZWQZiXtStu1A3rf5xAPsmKq3BBXTPdyc
set DO_STORAGE_ENDPOINT=sgp1.digitaloceanspaces.com
set DO_STORAGE_ENDPOINT_REGION=sgp1
set DO_STORAGE_BUCKETNAME=jin-tfip-nus 
mvn clean spring-boot:run
```

## Client side
```
ng serve --proxy-config proxy-config.js
```

## Railway Deployment
```
ng build
```
copy all files on the dist folder to the server static folder