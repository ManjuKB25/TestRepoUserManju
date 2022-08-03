Sample implementation of Users REST API

Implemented:
1) Get of users
2) Filter by name and/or age, along with sort feature
3) Implemented V2 vesrion for finegrained user api.
4) Added basic authentication for accessing API for security.
5) Incorporated exception handling, logging and little bit of unit testing.
6) Swagger and UI is not included in this yet.


non-Functional:
1) need to test on load.
2) used mysql DB as data source

Further Enhancements:
1) Can conside consuming request parameters as a JSON object so that we can have wide range of filter and sort options.
2) Enhaced for multi-value acceptence for user and age fileds. Also can do a generalization on feilds for furture enhancement of User object.
