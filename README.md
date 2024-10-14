## Description 
this is experimental patient data managing microservices app with it's docker files in spring boot and vue js with reactive apigateway
## how to use it
- run `mvn istall` for each microservice unless vue witch is `npm istall`
- setup mysql database with Data.sql file and Mongodb database for riskmanager
- select active profile with `-Dspring.profiles.active` between prod if runnig on docker container or dev if runnig on windows in vmArgs for spring boot microservices
- run spring boot microservices with `java -jar -Dspring.profiles.active=dev `
- run vue js with `npm run dev` or `npm run build` then `npm run preview` for production
- run eurekaserver first and apigateway last 
- every api has sawagger ducumentation accessible directly
## Application architecture
![diagram](https://github.com/user-attachments/assets/5cdfff64-54ff-4522-b1fd-e113e83c2da0)
## Application preview
![image](https://github.com/user-attachments/assets/35a9f1ec-5955-495f-8093-e1fdda9b5684)
![image](https://github.com/user-attachments/assets/9dfd6437-395e-4b81-aecc-0bbec8bef974)
![image](https://github.com/user-attachments/assets/1d06c5a4-82f3-4b27-889d-4380a0c1f28b)
![image](https://github.com/user-attachments/assets/ba4affbf-ee47-41e9-bad5-c361607261e4)



