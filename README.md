# For local development
### Set up environment variables
```
# Config set up to use these environment variables directly when in local dev and in railway deployment
# LOCALHOST DEVELOPMENT

LOCALDEVELOPMENT=<leave blank>
MYSQLUSER=<mysql username>
MYSQLPASSWORD=<mysql password>
MYSQLDATABASE=<database name>
MYSQLHOST=<mysql host>
MYSQLPORT=<mysql port>
MYSQL_URL=jdbc:mysql://${MYSQLHOST}/${MYSQLDATABASE}

REDISHOST=<redis host>
REDISPORT=<redis port or 6379>
REDISUSERNAME=<redis username>
REDISPASSWORD=<redis password>
```
### Environment variables used in
[MySQL and Redis Configuration java file](https://github.com/Raynerenyar/paf_assessment/blob/main/src/main/java/sg/edu/nus/iss/app/assessment/config/Config.java)

# For Railway deployment
Use above variables and additionally insert in shared variable for `MYSQLDATABASE` as Railway defaults to `MYSQLDATABASE=railway`