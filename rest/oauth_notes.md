# OAuth Notes
* authenticate user
* delegating user authentication to the service to authorize third party client access
* 2LO: two-legged OAuth

## OAuth Roles:
* Client: Third Party Application
    * register with service
    * request by: identity + authorization grant
* Resource Owner
    * response: authorization grant
* Resource Server: API
* Authorization Server

## Client ID and Secret:
* Essentially equivalent to a username and password
* Client ID: public identifier for apps
```
* Example:
    Foursquare: ZYDPLLBWSK3MVQJSIYHB1OR2JXCY0X2C5UJ2QAR2MAAIT5Q
    Github: 6779ef20e75817b79602
    Google: 292085223830.apps.googleusercontent.com
    Instagram: f2a1ed52710d4533bde25be6da03b6e3
    SoundCloud: 269d98e4922fb3895e9ae2108cbb5064
    Windows Live: 00000000400ECB04
```
* Client Secret: known only to the application and the authorization server.


## Authorization grant
* Authorization Code: used with server-side Applications
    * most commonly used
* Implicit: mobile app
* Password Credentials: used with trusted Applications
* Client Credentials:
    * used with Applications API access
    * on behalf of the application itself rather than a user


## Steps:
1. Request for authorization code:
    * URL: https://authorization-server.com/auth?
    * Parameters: response_type=code&client_id=CLIENT_ID&redirect_uri=REDIRECT_URI&scope=photos&state=1234
        * response_type: code: authorization code
        * redirect_uri: redirect user to uri once authorization is complete
        * state: client application state, to verify response
2. Call back with the code:
    * https://example-app.com/cb?code=AUTH_CODE_HERE&state=1234zyx
3. Making authenticated requests:
    * curl -H "Authorization: Bearer RsT5OjbzRn430zqMLgV3Ia" ...example.com/1/me

## JWT: Json Web Tokens, "header.payload.signature"
* header: {"typ": "JWT", "alg": "HS256"}
* payload: claims about an entity (typically, the user)
    * {"userId": "b08f86af-35da-48f2-8fab-cef3904660bd"}
    * type:
        * reserved: iss (issuer), exp (expiration time), sub (subject), aud (audience)
        * public:
        * private:
* signature: Hash(base64urlEncode(header) + “.” + base64urlEncode(payload), secret_key)


### References:
* https://aaronparecki.com/oauth-2-simplified/
* https://www.oauth.com/oauth2-servers/client-registration/client-id-secret/
