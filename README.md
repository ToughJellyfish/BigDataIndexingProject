### Below are the command lines for this project. Authorization parts are varied. ETag should be changed each time. 

## DELETE
`
curl -i -X DELETE -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlcldLaFUiLCJleHAiOjE1MjY3MTAwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMTc2YWEyNi03ZWM0LTQ0ZjItOGU1ZC0yYWYxZDc4NTE4Y2MiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.RrPBNi3bQ5FjJrqntWmve57SCajW1p0sID0UaVbm_hw" http://localhost:8080/1/plans/12xvxc345ssdsds
`

## Expired Token
`
curl -i -H "Accept:application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlckpPYUciLCJleHAiOjE1MjE2NjM2MTAsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJmNGU4YmUzOC1lZTMzLTQ3NzMtYmQyZC05YjhmMzljYTFmMTYiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.GPYg1OjURosogNtW-yVlLzIZ9tIkcygrny_ukVJhhbc" -H "Content-type:application/json" -X POST -d @usecase.JSON http://localhost:8080/plans
`

## POST
`
curl -i -H "Accept:application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlcldLaFUiLCJleHAiOjE1MjY3MTAwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMTc2YWEyNi03ZWM0LTQ0ZjItOGU1ZC0yYWYxZDc4NTE4Y2MiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.RrPBNi3bQ5FjJrqntWmve57SCajW1p0sID0UaVbm_hw" -H "Content-type:application/json" -X POST -d @usecase.JSON http://localhost:8080/plans
`

## GET
`
curl -i -H "Accept:application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlcldLaFUiLCJleHAiOjE1MjY3MTAwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMTc2YWEyNi03ZWM0LTQ0ZjItOGU1ZC0yYWYxZDc4NTE4Y2MiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.RrPBNi3bQ5FjJrqntWmve57SCajW1p0sID0UaVbm_hw" -H 'If-None-Match: "01c2fdd22be1181ef5bc42816f29125f1"' -H "Content-type:application/json" -X GET http://localhost:8080/1/plan/12xvxc345ssdsds
`

## GET AGAGIN FOR ETAG
`
curl -i -H "Accept:application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlcldLaFUiLCJleHAiOjE1MjY3MTAwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMTc2YWEyNi03ZWM0LTQ0ZjItOGU1ZC0yYWYxZDc4NTE4Y2MiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.RrPBNi3bQ5FjJrqntWmve57SCajW1p0sID0UaVbm_hw" -H 'If-None-Match: "01c2fdd22be1181ef5bc42816f29125f1"' -H "Content-type:application/json" -X GET http://localhost:8080/1/plan/12xvxc345ssdsds
`

## JSON VALIDATION CHANGED INTEGER TO STRING 175:USECASE1
`
curl -i -H "Accept:application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlcldLaFUiLCJleHAiOjE1MjY3MTAwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMTc2YWEyNi03ZWM0LTQ0ZjItOGU1ZC0yYWYxZDc4NTE4Y2MiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.RrPBNi3bQ5FjJrqntWmve57SCajW1p0sID0UaVbm_hw" -H "Content-type:application/json" -X POST -d @usecase1.JSON http://localhost:8080/plans
`

## PUT WHOLE PROJECT 
`
curl -i -H "Accept:application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlcldLaFUiLCJleHAiOjE1MjY3MTAwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMTc2YWEyNi03ZWM0LTQ0ZjItOGU1ZC0yYWYxZDc4NTE4Y2MiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.RrPBNi3bQ5FjJrqntWmve57SCajW1p0sID0UaVbm_hw" -H "Content-type:application/json" -X PUT -d @usecase3.JSON http://localhost:8080/plans
`

## PATCH s2.json demoBoolean
`
curl -i -H "Accept:application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlcldLaFUiLCJleHAiOjE1MjY3MTAwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMTc2YWEyNi03ZWM0LTQ0ZjItOGU1ZC0yYWYxZDc4NTE4Y2MiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.RrPBNi3bQ5FjJrqntWmve57SCajW1p0sID0UaVbm_hw" -H "Content-type:application/json" -X PATCH -d @s2.json http://localhost:8080/object/plan
`

## PUT WHOLE PROJECT 
`
curl -i -H "Accept:application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0VXNlciIsInNjb3BlIjpbImJhciIsInJlYWQiLCJ3cml0ZSJdLCJvcmdhbml6YXRpb24iOiJ0ZXN0VXNlcldLaFUiLCJleHAiOjE1MjY3MTAwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMTc2YWEyNi03ZWM0LTQ0ZjItOGU1ZC0yYWYxZDc4NTE4Y2MiLCJjbGllbnRfaWQiOiJiYXJDbGllbnRJZFBhc3N3b3JkIn0.RrPBNi3bQ5FjJrqntWmve57SCajW1p0sID0UaVbm_hw" -H "Content-type:application/json" -X PUT -d @usecase3.JSON http://localhost:8080/plans
`

### KIBANA

Set up Elasticsearch and kibana to demostrate the elastic search plan as a whole object

`
GET index_all/plan/12xvxc345ssdsds
`
`
GET index_all/_search?q=linkedPlanServices.planserviceCostShares.copay:175
`
