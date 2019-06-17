docker build -t mysql-co2-status .

docker run --name mysql-co2-status -e MYSQL_ROOT_PASSWORD=my-secret-pw -p 3306:3306 -d mysql-co2-status:latest