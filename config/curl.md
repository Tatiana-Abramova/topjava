curl -i -X POST http://localhost:8080/topjava/users?userId=100000
curl -i http://localhost:8080/topjava/rest/meals

curl -i -X POST -d '{"dateTime": "2021-06-03T14:00:00","description": "Обед","calories": 1000}' \
  -H "Content-Type: application/json"  http://localhost:8080/topjava/rest/meals

curl -i -X PUT -d '{"id": 100009,"dateTime": "2021-07-03T14:00:00","description": "Обед","calories": 1000}' \
-H "Content-Type: application/json"  http://localhost:8080/topjava/rest/meals/100009

curl -i -X DELETE http://localhost:8080/topjava/rest/meals/100009

curl -i http://localhost:8080/topjava/rest/meals/100008

curl -i http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&endDate=2020-01-31&startTime=00:00&endTime=14:00


http://localhost:8080/topjava/rest/admin/users/100000/with-meals
http://localhost:8080/topjava/rest/profile/with-meals