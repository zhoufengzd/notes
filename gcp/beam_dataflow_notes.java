//////////////////////////////////////////////////////////////////////////////////////
// Architecture: pipelines, translators, runners
// 
// Beam SDK
//     Pipeline: streaming and processing logic. Pipelines form a chain of processes. 
//     PCollection: container of the data, flowing between each step of the pipeline.
//     Transform: PCollection => PCollection. 
//     Sink and Source: input (first step) of a pipeline
// 

// mvn dependency:sources

// Classes: 
//////////////////////////////////////////////////////////////////////////////////////

// PInput / POutput / PCollection
// ----------------------------------------------------------
public class TupleTag<V> implements Serializable {
  final String id;
  public TypeDescriptor<V> getTypeDescriptor() {
    return new TypeDescriptor<V>(getClass()) {};
  }
}
public abstract class TaggedPValue {
  public abstract TupleTag<?> getTag();
  public abstract PValue getValue();
}
public interface PInput {
  Pipeline getPipeline();
  List<TaggedPValue> expand();
}
public class PBegin implements PInput {
  public List<TaggedPValue> expand() { return Collections.emptyList(); }
}
public class KeyedPCollectionTuple<K> implements PInput {
  private final List<TaggedKeyedPCollection<K, ?>> keyedCollections;
  private final Coder<K> keyCoder;
  private final CoGbkResultSchema schema;
  private final Pipeline pipeline;
}
public interface POutput {
  Pipeline getPipeline();
  List<TaggedPValue> expand();

  void recordAsOutput(AppliedPTransform<?, ?, ?> transform);
}
public interface PValue extends POutput, PInput {
  String getName();
  void finishSpecifying(PInput upstreamInput, PTransform<?, ?> upstreamTransform);
}
public class PCollectionTuple implements PInput, POutput {
}
public abstract class TypedPValue<T> extends PValueBase implements PValue {
    private CoderOrFailure<T> coderOrFailure; // coder
    private TypeDescriptor<T> typeDescriptor;
    
    private static class CoderOrFailure<T> {
        @Nullable private final Coder<T> coder;
        @Nullable private final String failure;
    }
}
public class PCollection<T> extends TypedPValue<T> {
  private WindowingStrategy<?, ?> windowingStrategy;
  private IsBounded isBounded;
  public <OutputT> OutputT apply(String name, PTransform<...> t) {
    return Pipeline.applyTransform(name, this, t);
  }
}

// PipelineOptionsFactory / PipelineOptions / Pipeline 
// ----------------------------------------------------------

// PipelineOptions. Each attribute supports get / set access. set access are ignored / not listed in notes below
public interface PipelineOptions extends HasDisplayData {
    // Key attribute. Like DirectRunner, DataflowRunner
    getRunner();
    void setRunner(...); 

    String getTempLocation();
    String getJobName();
}

public interface GcpOptions extends GoogleApiDebugOptions, PipelineOptions {
  String getProject();  // Project id
  String getZone();
  Credentials getGcpCredential();
  String getGcpTempLocation();  // A GCS path for storing temporary files in GCP.
}

public interface GcsOptions extends ApplicationNameOptions, GcpOptions, PipelineOptions {
  GcsUtil getGcsUtil();
  ExecutorService getExecutorService();
  String getGcsEndpoint();  // The URL for the GCS API.
  Integer getGcsUploadBufferSizeBytes();
  PathValidator getPathValidator();
}

public interface PubsubOptions extends GcpOptions, PipelineOptions, StreamingOptions {
    String getPubsubRootUrl();
}

public interface BigQueryOptions extends GcpOptions, PipelineOptions, StreamingOptions {
  String getTempDatasetId();
}

public interface DataflowPipelineOptions extends PipelineOptions, BigQueryOptions, GcpOptions,  GcsOptions, PubsubOptions {
    String getStagingLocation();
    String getTemplateLocation();  // Where the runner should generate a template file
}

public class Pipeline {
  public static Pipeline create(PipelineOptions options) {
    return new Pipeline(PipelineRunner.fromOptions(options), options);
  }
  
  public PBegin begin() { return PBegin.in(this); }
  
  public <OutputT extends POutput> OutputT apply(String name, PTransform<? super PBegin, OutputT> root) {
    return begin().apply(name, root);
  }
  public static <PInput, POutput> OutputT applyTransform(String name, InputT input, PTransform<...> transform) {
    return input.getPipeline().applyInternal(name, input, transform);
  }
  
  private <InputT, OutputT> OutputT applyInternal(String name, InputT input, PTransform<...> transform) {
    String namePrefix = transforms.getCurrent().getFullName();
    String uniqueName = uniquifyInternal(namePrefix, name);

    transforms.pushNode(uniqueName, input, transform);
    transforms.finishSpecifyingInput();
    transform.validate(input);
    OutputT output = transform.expand(input);
    transforms.setOutput(output);

    return output;
  }  
  
  public PipelineResult run() {
      return runner.run(this);
  }
  private final PipelineRunner<?> runner;
  private final PipelineOptions options;
  private final TransformHierarchy transforms = new TransformHierarchy(this);
  private Collection<PValue> values = new ArrayList<>();
  private Set<String> usedFullNames = new HashSet<>();
  private CoderRegistry coderRegistry;
}

// Source data: TextIO
// ----------------------------------------------------------
public class TextIO {
    public static class Read {
        public static Bound<String> from(String filepattern) {
            return new Bound<>(DEFAULT_TEXT_CODER).from(filepattern);
        }
    }
    public static class Write {
    }
    static class TextSource<T> extends FileBasedSource<T>  {
    }
    static class TextSink<T> extends FileBasedSink<T> {
    }
}
public class Read {
    public static class Bounded<T> extends PTransform<PBegin, PCollection<T>> {
        private final BoundedSource<T> source;

        public final PCollection<T> expand(PBegin input) {
          return PCollection.<T>createPrimitiveOutputInternal(input.getPipeline(),
              WindowingStrategy.globalDefault(), IsBounded.BOUNDED)
              .setCoder(getDefaultOutputCoder());
        }
    }
}

// DirectRunner: In-Memory implementation of the Dataflow Programming Model
// ----------------------------------------------------------
public class DirectRunner extends PipelineRunner<DirectPipelineResult> {
  private final DirectOptions options;
  private final Set<Enforcement> enabledEnforcements;
  private Supplier<Clock> clockSupplier = new NanosOffsetClockSupplier();
  
  public DirectPipelineResult run(Pipeline pipeline) {
    ...
    pipeline.traverseTopologically(keyedPValueVisitor);
    ...
    DirectGraph graph = graphVisitor.getGraph();

    RootProviderRegistry rootInputProvider = RootProviderRegistry.defaultRegistry(context);
    PipelineExecutor executor = ExecutorServiceParallelExecutor.create(
                options.getTargetParallelism(), graph,
                rootInputProvider,
                registry,
                Enforcement.defaultModelEnforcements(enabledEnforcements),
                context);
    executor.start(graph.getRootTransforms());
  }  
}

// PTransform
// ----------------------------------------------------------

// The Dataflow SDKs define the following core transforms:
//     ParDo for generic parallel processing
//     GroupByKey for Key-Grouping Key/Value pairs
//     Combine for combining collections or grouped values
//     Flatten for merging collections

public abstract class PTransform<InputT extends PInput, OutputT extends POutput> {
}
public class CoGbkResultSchema implements Serializable {
  private final TupleTagList tupleTagList;
  private final HashMap<TupleTag<?>, Integer> tagMap = new HashMap<>(); // TupleTags => union tags
}

public class SimpleDoFnRunner<InputT, OutputT> implements DoFnRunner<InputT, OutputT> {
  private final DoFn<InputT, OutputT> fn;
  private final DoFnInvoker<InputT, OutputT> invoker;
  private final DoFnContext<InputT, OutputT> context;
  
  private final OutputManager outputManager;
  private final TupleTag<OutputT> mainOutputTag;
  private final boolean observesWindow;
  private final DoFnSignature signature;
  private final Coder<BoundedWindow> windowCoder;
  private final StepContext stepContext;
  
  private void invokeProcessElement(WindowedValue<InputT> elem) {
    final DoFnProcessContext<InputT, OutputT> processContext = createProcessContext(elem);
    invoker.invokeProcessElement(processContext);
  }  
}

public abstract class DoFn<InputT, OutputT> implements Serializable, HasDisplayData {
  protected Map<String, DelegatingAggregator<?, ?>> aggregators = new HashMap<>();
  
  public abstract class Context {}
}

public class ParDo {
  public static <InputT, OutputT> Bound<InputT, OutputT> of(DoFn<InputT, OutputT> fn) {
    validate(fn);
    return new Unbound().of(fn, displayDataForFn(fn));
  }
  
  public static class Unbound {
    private final String name;
    private final List<PCollectionView<?>> sideInputs;
    public <InputT, OutputT> Bound<InputT, OutputT> of(DoFn<InputT, OutputT> fn) {
      validate(fn);
      return of(fn, displayDataForFn(fn));
    }
    private <InputT, OutputT> Bound<InputT, OutputT> of(
        Serializable originalFn, DisplayData.ItemSpec<? extends Class<?>> fnDisplayData) {
      return new Bound<>(name, originalFn, sideInputs, fnDisplayData);
    }
  }
  
  public static class Bound<InputT, OutputT> extends PTransform<PCollection<? extends InputT>, PCollection<OutputT>> {
    private final List<PCollectionView<?>> sideInputs;
    private final Serializable fn;
    private final DisplayData.ItemSpec<? extends Class<?>> fnDisplayData;
  }    
}

class TransformExecutor<T> implements Runnable {
  private final TransformEvaluatorFactory evaluatorFactory;
  private final Iterable<? extends ModelEnforcementFactory> modelEnforcements;

  /** The transform that will be evaluated. */
  private final AppliedPTransform<?, ?, ?> transform;
  /** The inputs this {@link TransformExecutor} will deliver to the transform. */
  private final CommittedBundle<T> inputBundle;

  private final CompletionCallback onComplete;
  private final TransformExecutorService transformEvaluationState;
  private final EvaluationContext context;
  
  public void run() {
    MetricsContainer metricsContainer = new MetricsContainer(transform.getFullName());
    ...
    processElements(evaluator, metricsContainer, enforcements);

    finishBundle(evaluator, metricsContainer, enforcements);
    transformEvaluationState.complete(this);
  }
  
  private void processElements(TransformEvaluator<T> eva, MetricsContainer m, Collection<ModelEnforcement<T>> enforcements) {
      // inputBundle.getElements() will open GoogleCloudStorageReadChannel / NetHttpResponse if it's gs store
      for (WindowedValue<T> value : inputBundle.getElements()) {
        for (ModelEnforcement<T> enforcement : enforcements) {
          enforcement.beforeElement(value);
        }

        eva.processElement(value);

        for (ModelEnforcement<T> enforcement : enforcements) {
          enforcement.afterElement(value);
        }
      }
  }
}

class WindowEvaluatorFactory implements TransformEvaluatorFactory {
    private static class WindowIntoEvaluator<InputT> implements TransformEvaluator<InputT> {
        private final AppliedPTransform<PCollection<InputT>, PCollection<InputT>, Assign<InputT>> transform;
        private final WindowFn<InputT, ?> windowFn;
        private final UncommittedBundle<InputT> outputBundle;

        public void processElement(WindowedValue<InputT> compressedElement) throws Exception {
            Iterator var2 = compressedElement.explodeWindows().iterator();
            while(var2.hasNext()) {
                WindowedValue element = (WindowedValue)var2.next();  // WindowedValue = timestamp + value + pane
                Collection windows = this.assignWindows(this.windowFn, element);
                this.outputBundle.add(WindowedValue.of(element.getValue(), element.getTimestamp(), windows, element.getPane()));
            }
        }
    }
}

// PubSub
// ----------------------------------------------------------

public abstract class StandardCoder<T> implements Coder<T> {
}

public abstract class DeterministicStandardCoder<T> extends StandardCoder<T> {
}

public abstract class AtomicCoder<T> extends DeterministicStandardCoder<T> {
}

public class StringUtf8Coder extends AtomicCoder<String> {
}
