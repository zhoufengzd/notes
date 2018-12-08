# Python Flask Notes
* micro web framework = Werkzeug toolkit (WSGI) + Jinja2

## command line
```
flask db init
flask db migrate
flask db upgrade
```

## classes
### flask_restplus
* define and document endpoints
* validate input
* format output (as JSON)
* turn Python exceptions into HTTP responses
* minimise boilerplate code
* generate swagger documentation

```
flask_restplus/api.py:
----------------------
class Api(object):
    def _init_app(self, app):
        self._register_specs(self.blueprint or app)  ## SwaggerView, /swagger.json
        self._register_doc(self.blueprint or app)  ## possibly doc view

        app.handle_exception = partial(self.error_router, app.handle_exception)
        app.handle_user_exception = partial(self.error_router, app.handle_user_exception)

        for resource, urls, kwargs in self.resources:
            self._register_view(app, resource, *urls, **kwargs)

        self._register_apidoc(app)

    @property
    def payload(self):
        return request.get_json()

    def add_namespace(self, ns, path=None):

    def mediatypes_method(self):
        '''Return a method that returns a list of mediatypes'''
        return lambda resource_cls: self.mediatypes() + [self.default_mediatype]

    def __getattr__(self, name):    # hook to namespace method
        return getattr(self.default_namespace, name)

```

## call stack
```
##  dispatch request: base
ThreadingMixIn::process_request_thread()  ##-- SocketServer.py
BaseHTTPRequestHandler.handle():  ##-- BaseHTTPServer.py
    self.handle_one_request()
WSGIRequestHandler::handle_one_request():  ##-- werkzeug/serving.py
    self.run_wsgi()
WSGIRequestHandler::run_wsgi(self):
    execute(self.server.app)    ##-- flask app
Flask::wsgi_app(self, environ, start_response):     ##-- flask/app.py
    response = self.full_dispatch_request()
Flask::dispatch_request(self)
    return self.view_functions[rule.endpoint](**req.view_args)

##  dispatch request: restplus
### -- setup
app = Flask(__name__)
api = Api(app)
responseModel = api.model('<response tag>',{
    '_type' : fields.Float(min=0, max=1,description='<>'',
})

### call stack: dispatch by Resource
View::as_view()     ## flask/views.py
    dispatch_request()
MethodView(MethodViewType, View)::dispatch_request():
    meth = getattr(self, request.method.lower(), None)  ## rest methods, like "get", "post", etc
    return meth(*args, **kwargs)
Resource(MethodView)::dispatch_request()     ## flask_restplus/resource.py
    meth = getattr(self, request.method.lower(), None)
    for decorator in self.method_decorators:
        meth = decorator(meth)
    resp = meth(*args, **kwargs)
    mediatype = request.accept_mimetypes.best_match(representations, default=None)
    if mediatype in representations:
        data, code, headers = unpack(resp)
        resp = representations[mediatype](data, code, headers)
        resp.headers['Content-Type'] = mediatype
    return resp

##  test
app = Flask(__name__, template_folder='templates')
app_client = app.test_client()
app_client.get('/ars/by_asset?listing_uids=...')
```
