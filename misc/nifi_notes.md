# Nifi Notes

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
    * flowfile repository: active flowfile
        * journals: event journals
        * swap:
    * content repository: template
    * provenance repository:
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

## references:
* https://nifi.apache.org/docs/nifi-docs/html/nifi-in-depth.html
* https://cwiki.apache.org/confluence/display/nifi/example+dataflow+templates
* https://github.com/alexsjones/kubernetes-nifi-cluster
* https://docs.hortonworks.com/hdpdocuments/hdf3/hdf-3.1.0/bk_administration/content/clustering.html
