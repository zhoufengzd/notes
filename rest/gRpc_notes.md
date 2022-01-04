# gRpc Notes
* HTTP/2. server / client / bi-direction streaming

## gRpc Usage:
* simple RPC: function call and wait
    * `rpc GetFeature(Point) returns (Feature) {}`
* server-side streaming RPC
    * `rpc ListFeatures(Rectangle) returns (stream Feature) {}`
* client-side streaming RPC
    * `rpc RecordRoute(stream Point) returns (RouteSummary) {}`
* bidirectional streaming RPC
    * `rpc RouteChat(stream RouteNote) returns (stream RouteNote) {}`

