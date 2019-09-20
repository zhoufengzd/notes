# Nifi Notes
* dataflow (ETL) without code
* nifi is java 1.8 only as of 2019-04-25
    * nifi.sh start
    * bootstrap.conf:
        * java.arg.debug="... address=8000"

## components:
* web server: jetty
* flow controller
    * process groups: processors + connections
        * processor + connections (config) + downstream processor
        * connection: fifo, lifo queue, etc
    * controller service: outside dataflow, like ssl certificates, jdbc connection.
* extensions - various plugins to interact with other system
    * @work/nar/extensions
* repositories
    * database repository: nifi flow / user / keys
    * flowfile repository: active flowfile, state of flowfile in the flow
        * journals: event journals
        * swap:
    * content repository: actual content bytes of flowfile
    * provenance repository: provenance event data
* config: flow.xml.gz, nifi.properties

## funnels

## cluster
* no master node, auto primary node
* flow election
* distribution the load: list -> fetch pattern
    * by pushing: use load balancer to push to different nifi node
        * list: all the work items. running on the primary node.
        * fetch / push to different node with single item
        * remote process group (rpg): input port / output port
    * by pulling: each nifi node pulls directly from data producer. e.g., kafka.
* site to site:
    * from one nifi cluster (like local) to another nifi cluster (e.g., global)

## terminology
* dataflow manager (dfm): a nifi user manages dataflow
* flowfile: content + attributes
* processor: ETL processor
* relationship: processor result
* funnel: a nifi component to combine the data from several connections into a single connection.

## implementation
```
# locate \/nifi\/nifi\/ | grep -v \/target\/ | grep \.java | grep Port
# locate \/nifi\/nifi\/ | grep -v \/target\/ | grep bin\/ | grep "\.sh"
-> ... | sed "s#/Users/fzhou/workspace/test/_downloaded/nifi/nifi#...#g"
```

### core
```
* -- FlowEngine: threaded scheduler
public final class FlowEngine extends ScheduledThreadPoolExecutor {
    public FlowEngine(int corePoolSize...) {...}
    protected void beforeExecute() {...}
    protected void schedule() {...}
    protected void afterExecute() {...}
}

* -- LocalPort: to transfer flow file between local process group
public class LocalPort extends AbstractPort {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    final int maxIterations;

    public void onTrigger(final ProcessContext context, final ProcessSession session) {
        available = context.getAvailableRelationships();
        while (available) {
            final List<FlowFile> flowFiles = session.get(1000);
            session.transfer(flowFiles, Relationship.ANONYMOUS);
            session.commit();
        }
    }
```

### core-api
```
* -- ConnectableType
public enum ConnectableType {
    PROCESSOR,
    INPUT_PORT,
    OUTPUT_PORT,
    FUNNEL
}

* -- Connectable
public interface Connectable extends Triggerable {
    Set<Connection> getConnections(relationship);
    ProcessGroup getProcessGroup();
}

* -- Funnel
public interface Funnel extends Connectable {
    void setScheduledState(scheduledState);
}

* -- Port
public interface Port extends Connectable {
    void shutdown();
    boolean isValid();
    void onSchedulingStart();
}

* -- ProcessGroup
public interface ProcessGroup {
    ProcessGroup getParent();
    void startProcessing();

    Set<Port> getInputPorts();
    Set<Port> getOutputPorts();

    Set<Connection> getConnections();
    Set<ControllerServiceNode> findAllControllerServices();
}
```

### api
```
* -- FlowFile: immutable / thread safe
public interface FlowFile extends Comparable<FlowFile> {
    long getId();
    long getEntryDate();
    ...
    long getLineageStartIndex();
    ...
    String getAttribute(String key);
}

* -- FlowFile state
public enum ProvenanceEventType {
    // init by create, receive, fetch
    CREATE, RECEIVE, FETCH, DOWNLOAD,
    // then...
    FORK, JOIN, CLONE, ATTRIBUTES_MODIFIED, ADDINFO,
    // finally
    DROP, EXPIRE,
}

* -- ProcessContext
public interface ProcessContext extends PropertyContext {
    Map<PropertyDescriptor, String> getProperties();

* -- Processors:
public interface Processor extends ConfigurableComponent
    void onTrigger(ProcessContext context, ProcessSessionFactory sessionFactory);

* -- ProcessSession
public interface ProcessSession {
    void commit();
    void rollback(); // before commit
    void migrate(ProcessSession newOwner, Collection<FlowFile> flowFiles);

    FlowFile get();
    FlowFile create();

    void read(FlowFile source, InputStreamCallback reader);
    FlowFile write(FlowFile source, OutputStreamCallback writer);
    FlowFile merge(Collection<FlowFile> sources, ...)
}

* -- Controls data flow rate to follow-on processors
public class ControlRate extends AbstractProcessor {
    public void onTrigger(...)  {
        List<FlowFile> flowFiles = session.get(new ThrottleFilter(MAX_FLOW_FILES_PER_BATCH));
        ...
        for (FlowFile flowFile : flowFiles)
            session.transfer(flowFile, REL_SUCCESS);
    }
```

### standard processors
```
* -- GenerateFlowFile: creates FlowFiles for testing
public class GenerateFlowFile extends AbstractProcessor {
    private final AtomicReference<byte[]> data = new AtomicReference<>();

    public void onTrigger(final ProcessContext context, final ProcessSession session) {
        final byte[] data;
        if (context.getProperty(UNIQUE_FLOWFILES))
            data = generateData(context);
        else
            data = context.getProperty(CUSTOM_TEXT);
        ...
        session.transfer(flowFile, SUCCESS);
    }
}

* -- MergeRecord:
*      merges multiple FlowFiles into one
*      convert format: json -> avro, etc.
public class MergeRecord extends AbstractSessionFactoryProcessor {
}

* -- SplitContent
public class SplitContent extends AbstractProcessor {
    // Splits incoming FlowFiles by a specified byte sequence
    public void onTrigger(...) {
        FlowFile flowFile = session.get();
        final List<Tuple<Long, Long>> splits = new ArrayList<>();
        byteSequence = context.getProperty(BYTE_SEQUENCE)..
        buffer = new NaiveSearchRingBuffer(byteSequence);
        InputStream in = new BufferedInputStream(rawIn);
        while (true) {
            final int nextByte = in.read();
            bytesRead++;
            if (buffer.addAndCompare(nextByte)) {
                ...
                splits.add(new Tuple<>(splitStart, splitLength));
            }
        }
        session.transfer(splits, ...);
    }
}

* -- Validates FlowFile records against schema
public class ValidateRecord extends AbstractProcessor {
}

* -- AbstractJsonPathProcessor
public abstract class AbstractJsonPathProcessor
    static DocumentContext validateAndEstablishJsonContext(ProcessSession processSession, FlowFile flowFile)

* -- EvaluateJsonPath:
*      writes to json: flowfile-content, or scalar: flowfile-attribute
public class EvaluateJsonPath extends AbstractJsonPathProcessor {
    public void onTrigger(...) {
        FlowFile flowFile = processSession.get();
        documentContext = validateAndEstablishJsonContext(processSession, flowFile);
        if (destinationIsAttribute)
            jsonPathResults.put(jsonPathAttrKey, resultRepresentation);
        else
            processSession.write(flowFile, resultRepresentation);
    }
}

* -- RouteOnAttribute
public class RouteOnAttribute extends AbstractProcessor {
    PropertyDescriptor ROUTE_STRATEGY = ROUTE_ALL_MATCH | ROUTE_ANY_MATCHES

    public void onTrigger(final ProcessContext context, final ProcessSession session) {
        FlowFile flowFile = session.get();
        while (relationshipNameIterator.hasNext())
            transferMap.put(relationship, cloneFlowFile);

        // now transfer clone to mapped relationships
        for (final Map.Entry<Relationship, FlowFile> entry : transferMap.entrySet())
            session.transfer(updatedFlowFile...);

        //now transfer the original flow file
        session.transfer(flowFile, firstRelationship);
    }
}

* -- GetHTTP
*      deprecated. Use InvokeHTTP instead.
public class GetHTTP extends AbstractSessionFactoryProcessor
```

### serialization services
```
* -- SchemaRegistryService:
public abstract class SchemaRegistryService extends AbstractControllerService {
}

* -- JsonTreeReader:
*      Parses JSON / JSON array into individual Record objects against schema
*      Will skip fileds that are not defined in the schema
public class JsonTreeReader extends SchemaRegistryService implements RecordReaderFactory {
}
```

### gcp processors
```
* BigQuery
public class PutBigQueryBatch extends AbstractBigQueryProcessor {
    public void onTrigger(...) {
        FlowFile flowFile = session.get();

        projectId = context.getProperty(PROJECT_ID)..;
        dataset = context.getProperty(DATASET)..;
        tableName = context.getProperty(TABLE_NAME)..;
        schema = schemaFromString(context.getProperty(TABLE_SCHEMA)..);

        TableDataWriteChannel writer = getCloudService().writer(...)
        session.read(flowFile, rawIn -> {
            ReadableByteChannel readableByteChannel = Channels.newChannel(rawIn);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            while (readableByteChannel.read(byteBuffer) >= 0) {
                byteBuffer.flip();
                writer.write(byteBuffer);
                byteBuffer.clear();
            }
        };
}

public class PutGCSObject extends AbstractGCSProcessor {
    public void onTrigger(...) {
        FlowFile flowFile = session.get();
        bucket = context.getProperty(BUCKET);
        key = context.getProperty(KEY);
        Storage storage = getCloudService();
        session.read(flowFile, new InputStreamCallback() {
            @Override
            public void process(InputStream rawIn) {
                InputStream ins = new BufferedInputStream(rawIn)
                BlobId id = BlobId.of(bucket, key);
                BlobInfo.Builder builder = BlobInfo.newBuilder(id);

                Blob blob = storage.create(builder.build(), ins, blobWriteOptions)
            });
        String url = "https://" + bucket + ".storage.googleapis.com/" + key;
        session.getProvenanceReporter().send(flowFile, url, ...);
    }
}

* GCS
public class ListGCSBucket extends AbstractGCSProcessor {
    public void onTrigger(...) {
        String bucket = context.getProperty(BUCKET)..;
        String prefix = context.getProperty(PREFIX)..;

        // 1. list blobs under bucket and build flow files.
        // 2. maintain state so the same file won't be picked up twice
        Map<String, String> state = new HashMap<>();
        state.put(CURRENT_TIMESTAMP, String.valueOf(currentTimestamp));

        Page<Blob> blobPages = storage.list(bucket, listOptions);
        for (Blob blob : blobPages.getValues()) {
            i++;
            state.put("key-"+i, blob.getName());

            attributes.put(CoreAttributes.FILENAME.key(), blob.getName());
            attributes.put(UPDATE_TIME_ATTR, String.valueOf(blob.getUpdateTime()));

            FlowFile flowFile = session.create();
            flowFile = session.putAllAttributes(flowFile, attributes);
            session.transfer(flowFile, REL_SUCCESS);
        }

        context.getStateManager().setState(state, Scope.CLUSTER);
    }
}
```

### AMQP
```
abstract class AMQPWorker implements AutoCloseable {
    private Channel channel;
    protected Channel getChannel() { return channel; }
}

abstract class AbstractAMQPProcessor<T extends AMQPWorker> {
    private BlockingQueue<T> resourceQueue = new LinkedBlockingQueue<>();
    static {
        List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(HOST);
        properties.add(PORT);
        properties.add(USER);
        properties.add(PASSWORD);
        ...
    }

    @Override
    public void onTrigger(...) {
        AMQPResource<T> resource = createResource(context);
        processResource(...);
        resourceQueue.offer(resource);
    }
}

class AMQPConsumer extends AMQPWorker {
    private String queueName;
    private Consumer consumer;

    AMQPConsumer(Connection connection, String queueName, ...){
        channel.basicConsume(queueName, autoAcknowledge, consumer);
    }
}

public class ConsumeAMQP extends AbstractAMQPProcessor<AMQPConsumer> {
    void processResource(connection, consumer, context, session) {
        for (int i = 0; i < context.getProperty(BATCH_SIZE); i++) {
            GetResponse response = consumer.consume();
            FlowFile flowFile = session.create();
            flowFile = session.write(flowFile, ... response.getBody());
            session.transfer(flowFile, REL_SUCCESS);
        }
        session.commit();
    }
}

class AMQPPublisher extends AMQPWorker {
    void publish(bytes, properties, routingKey, exchange) {
        getChannel().basicPublish(exchange, routingKey, .., properties, bytes);
    }
}

public class PublishAMQP extends AbstractAMQPProcessor<AMQPPublisher> {
    protected void processResource(connection, publisher, context, session) {
        FlowFile flowFile = session.get();
        BasicProperties amqpProperties = extractAmqpPropertiesFromFlowFile(flowFile);
        String routingKey = context.getProperty(ROUTING_KEY)..;
        String exchange = context.getProperty(EXCHANGE)..;
        byte[] messageContent = extractMessage(flowFile, session);

        publisher.publish(messageContent, amqpProperties, routingKey, exchange);
    }

    private BasicProperties extractAmqpPropertiesFromFlowFile(FlowFile flowFile) {
        // amqp properties, prefix with "amqp$", like "amqp$contentType"
        final AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();

        updateBuilderFromAttribute(flowFile, "contentType", builder::contentType);
        updateBuilderFromAttribute(flowFile, "contentEncoding", builder::contentEncoding);
        updateBuilderFromAttribute(flowFile, "deliveryMode", mode -> builder.deliveryMode(Integer.parseInt(mode)));
        updateBuilderFromAttribute(flowFile, "priority", pri -> builder.priority(Integer.parseInt(pri)));
        updateBuilderFromAttribute(flowFile, "correlationId", builder::correlationId);
        updateBuilderFromAttribute(flowFile, "replyTo", builder::replyTo);
        updateBuilderFromAttribute(flowFile, "expiration", builder::expiration);
        updateBuilderFromAttribute(flowFile, "messageId", builder::messageId);
        updateBuilderFromAttribute(flowFile, "timestamp", ts -> builder.timestamp(new Date(Long.parseLong(ts))));
        updateBuilderFromAttribute(flowFile, "type", builder::type);
        updateBuilderFromAttribute(flowFile, "userId", builder::userId);
        updateBuilderFromAttribute(flowFile, "appId", builder::appId);
        updateBuilderFromAttribute(flowFile, "clusterId", builder::clusterId);
        updateBuilderFromAttribute(flowFile, "headers", headers -> builder.headers(validateAMQPHeaderProperty(headers)));

        return builder.build();
    }

}

```

## references:
### unofficial
* https://touk.pl/blog/2018/07/19/what-really-grinds-my-gears-apache-nifi
* https://www.nifi.rocks
* https://dzone.com/articles/apache-nifi-10-cheatsheet

### official
* https://nifi.apache.org/docs/nifi-docs/html/nifi-in-depth.html
* https://cwiki.apache.org/confluence/display/nifi/example+dataflow+templates
* https://github.com/alexsjones/kubernetes-nifi-cluster
* https://docs.hortonworks.com/hdpdocuments/hdf3/hdf-3.1.0/bk_administration/content/clustering.html
