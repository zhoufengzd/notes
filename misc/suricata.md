# suricata notes
* network threat detection engine

## Engine:
* intrusion detection (IDS)
* intrusion prevention (IPS)
* network security monitoring (NSM)
* offline pcap processing

###  Suricata Flow
* flow: bidirectional
* netflow: unidirectional
* data: IP (source, destination), duration and volume, application layer

###  Suricata Flow Logging
* flow hash management is done asynchronously
* a flow is timed out after no packets have been seen for it for some time
* when a flow is timed out, it can be logged
* output to: file, syslog, redis, unix socket, lua script

## Config: Suricata.yaml
* memcap
* flow-timeouts: by protocol / state after last activity seen

## Internal

### struct
#### Packet: IP header + Data
* Protocol: ICMP / TCP / UDP, etc
* Time to Live (ttl)
* Source Address
* Destination Address

#### Flow
```
struct FlowConfig
{
    ...
    uint32_t hash_size;
    uint32_t max_flows;

    uint32_t timeout_new;
    uint32_t timeout_est;

    uint32_t emerg_timeout_new;
    uint32_t emerg_timeout_est;
    uint32_t emergency_recovery;
};

struct FlowKey
{
    Address src, dst;
    Port sp, dp;
    uint8_t proto;
    uint8_t recursion_level; /* dns recursion */
};

struct FlowAddress {
    union {
        uint32_t       address_un_data32[4];
        uint16_t       address_un_data16[8];
        uint8_t        address_un_data8[16];
    } address;
} ;

struct Flow
{
    FlowAddress src, dst;
    union {
        Port sp;         /**< tcp/udp source port */
        struct {
            uint8_t type;   /**< icmp type */
            uint8_t code;   /**< icmp code */
        } icmp_s;
    };
    union {
        Port dp;        /**< tcp/udp destination port */
        struct {
            uint8_t type;   /**< icmp type */
            uint8_t code;   /**< icmp code */
        } icmp_d;
    };
    uint8_t proto;
    uint8_t recursion_level;
    uint16_t vlan_id[2];

    ...
    /** Thread ID for the stream/detect portion of this flow */
    FlowThreadId thread_id;

    ...
    uint32_t flow_hash;
    struct timeval startts;
    struct timeval lastts; /* time stamp of last update (last packet) */

    /** flow tenant id, used to setup flow timeout and stream pseudo
     *  packets with the correct tenant id set */
    uint32_t tenant_id;

    /* Parent flow id for protocol like ftp */
    int64_t parent_id;

    AppProto alproto; /**< \brief application level protocol */
    void *alstate;      /**< application layer state */
    ...
};

enum FlowState {
    FLOW_STATE_NEW = 0,
    FLOW_STATE_ESTABLISHED,
    FLOW_STATE_CLOSED,
    FLOW_STATE_LOCAL_BYPASSED,
    FLOW_STATE_CAPTURE_BYPASSED,
};

struct FlowProtoTimeout {
    uint32_t new_timeout;
    uint32_t est_timeout;
    uint32_t closed_timeout;
    uint32_t bypassed_timeout;
} ;
```

### main flow:

#### flow:
##### flow-manager
```
static inline uint32_t FlowGetFlowTimeout(const Flow *f, enum FlowState state)
{
    uint32_t timeout;
    FlowProtoTimeoutPtr flow_timeouts = SC_ATOMIC_GET(flow_timeouts);
    switch(state) {
        default:
        case FLOW_STATE_NEW:
            timeout = flow_timeouts[f->protomap].new_timeout;
            break;
        case FLOW_STATE_ESTABLISHED:
            timeout = flow_timeouts[f->protomap].est_timeout;
            break;
        case FLOW_STATE_CLOSED:
            timeout = flow_timeouts[f->protomap].closed_timeout;
            break;
        case FLOW_STATE_CAPTURE_BYPASSED:
            timeout = FLOW_BYPASSED_TIMEOUT;
            break;
        case FLOW_STATE_LOCAL_BYPASSED:
            timeout = flow_timeouts[f->protomap].bypassed_timeout;
            break;
    }
    return timeout;
}
```

#### output:
```
-- ## output-json-flow.c
static json_t *CreateJSONHeaderFromFlow(const Flow *f, const char *event_type)
{
    json_t *js = json_object();
    ...
    struct timeval tv;
    TimeGet(&tv);
    CreateIsoTimeString(&tv, timebuf, sizeof(timebuf));
    json_object_set_new(js, "timestamp", json_string(timebuf));

    CreateJSONFlowId(js, (const Flow *)f);
    ...
}

static void JsonFlowLogJSON(JsonFlowLogThread *aft, json_t *js, Flow *f)
{
    LogJsonFileCtx *flow_ctx = aft->flowlog_ctx;
    json_t *hjs = json_object();

    JsonAddFlow(f, js, hjs);

    char timebuf2[64];
    CreateIsoTimeString(&f->lastts, timebuf2, sizeof(timebuf2));
    json_object_set_new(hjs, "end", json_string(timebuf2));

    int32_t age = f->lastts.tv_sec - f->startts.tv_sec;
    json_object_set_new(hjs, "age", json_integer(age));

    if (f->flow_end_flags & FLOW_END_FLAG_EMERGENCY)
        json_object_set_new(hjs, "emergency", json_true());
    const char *state = NULL;
    if (f->flow_end_flags & FLOW_END_FLAG_STATE_NEW)
        state = "new";
    else if (f->flow_end_flags & FLOW_END_FLAG_STATE_ESTABLISHED)
        state = "established";
    else if ...
        ...
    }
    json_object_set_new(hjs, "state", json_string(state));

    const char *reason = NULL;
    if (f->flow_end_flags & FLOW_END_FLAG_TIMEOUT)
        reason = "timeout";
    else if (f->flow_end_flags & FLOW_END_FLAG_FORCED)
        reason = "forced";
    else if (f->flow_end_flags & FLOW_END_FLAG_SHUTDOWN)
        reason = "shutdown";
    json_object_set_new(hjs, "reason", json_string(reason));

    json_object_set_new(js, "flow", hjs);

    if (f->proto == IPPROTO_TCP) {
        ...
        json_object_set_new(tjs, "tcp_flags_ts", json_string(hexflags));
        TcpSession *ssn = f->protoctx;
        const char *tcp_state = NULL;
        switch (ssn->state) {
            case TCP_LISTEN:
                tcp_state = "listen";
                break;
            ...
            case TCP_CLOSED:
                tcp_state = "closed";
                break;
        }
        json_object_set_new(tjs, "state", json_string(tcp_state));

        json_object_set_new(js, "tcp", tjs);
    }
}

static int JsonFlowLogger(ThreadVars *tv, void *thread_data, Flow *f)
{
    JsonFlowLogThread *jhl = (JsonFlowLogThread *)thread_data;

    json_t *js = CreateJSONHeaderFromFlow(f, "flow");
    JsonFlowLogJSON(jhl, js, f);

    OutputJSONBuffer(js, jhl->flowlog_ctx->file_ctx, &jhl->buffer);
    ...
}

void JsonFlowLogRegister (void)
{
    /* register as separate module */
    OutputRegisterFlowModule(LOGGER_JSON_FLOW, "JsonFlowLog", "flow-json-log",
        OutputFlowLogInit, JsonFlowLogger, JsonFlowLogThreadInit,
        JsonFlowLogThreadDeinit, NULL);

    /* also register as child of eve-log */
    OutputRegisterFlowSubModule(LOGGER_JSON_FLOW, "eve-log", "JsonFlowLog",
        "eve-log.flow", OutputFlowLogInitSub, JsonFlowLogger,
        JsonFlowLogThreadInit, JsonFlowLogThreadDeinit, NULL);
}

-- ## output-json.c
json_t *CreateJSONHeader(const Packet *p, enum OutputJsonLogDirection dir,
                         const char *event_type)
{
    json_t *js = json_object();

    char timebuf[64];
    const Flow *f = (const Flow *)p->flow;
    CreateIsoTimeString(&p->ts, timebuf, sizeof(timebuf));
    json_object_set_new(js, "timestamp", json_string(timebuf));

    CreateJSONFlowId(js, f);
    ...
}
```
