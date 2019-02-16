# rest api

## verbs:
* GET: idempotent.
    * code: 200(OK), 404(NOT FOUND), 400(BAD REQUEST)
* POST: create new subordinate resources, collections.
    * code: 201(Created), 200(OK) or 204(No Content).
    * may called only once.
* PUT: to update existing individual resource.
    * 201(Created), 200(OK) or 204(No Content).
    * may called multiple times for the same resource.
* DELETE: delete resources. 200(OK), 202(Accepted), 204(No Content)
* OPTIONS: list supported methods
* PATCH: make partial update on a resource. 200 (OK) or 204 (No Content), 404 (Not Found)
* -- Summary: 'POST', 'PUT', 'PATCH', 'DELETE' are considered submit methods.

### references
* https://realpython.com/python-ruby-and-golang-a-web-service-application-comparison/#martini-golang
* https://blog.usejournal.com/top-6-web-frameworks-for-go-as-of-2017-23270e059c4b
