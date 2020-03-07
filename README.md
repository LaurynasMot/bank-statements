Simply run the program if using IntelliJ  

If running from a command line this should do the trick:  
`mvn install`  
`mvn spring-boot:run`  
OR  
`java -jar target/bank-statements-0.0.1-SNAPSHOT.jar`  

Some basic data to test the program out:  

accountNumber,operationDate,beneficiary,comment,amount,currency  
LT667044060007944471,2020-03-05 20:55,VIADA LT,On Hold,-36.44,EUR  
LT667044060007944471,2020-03-03 13:35,RIMI UKMERGES G. 233,,-61.55,EUR  
LT667044060007944471,2020-03-02 11:10,MAXIMA LT,,750.14,IDR  
LT667044060007955584,2020-03-11 15:30,PAS ZILVINA,,-3.6,EUR  
LT667044060007944471,2020-03-12 03:15,MCDONALDS,,-13.8,EUR  
LT667044060007955584,2020-02-03 11:57,Hesburger,,658.13,EUR  
LT667044060198713547,2019-08-31 22:12,Kavine Spunka Etmonu,,-2.8,EUR  

And for dates - this is a basic json of dates:  
{  
	"dateFrom" : "2020-03-01T09:15",  
	"dateTo" : "2020-03-05T21:15"  
}  

Examples of possible POST and GET:  
POST localhost:8080/statements/  
GET localhost:8080/statements/  
GET localhost:8080/statements/json/  
GET localhost:8080/statements/LT667044060007944471  
  
I was using Postman to do the requests.
