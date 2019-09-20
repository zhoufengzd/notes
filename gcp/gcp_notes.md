# Google Cloud Platform (GCP)

## Access and Deployment
* Google Compute Engine = IaaS service providing virtual machines similar to Amazon EC2
    * Container Engine --
* Google App Engine – PaaS service for directly hosting applications similar to AWS Elastic Beanstalk
* Container Cluster - Kubernetes docker container
* Google Cloud Functions – similar to Amazon Lambda or IBM OpenWhisk
    * Cloud Libraries: idomatic, with gcloud-based emulators
    * API Libraries

## Main components
* BigQuery
    * data warehouse
    * columnar data format
    * fast! for large (terabytes) data
* BigTable
    * no acid transactions / not highly structured
    * low latency data access, used by search, gmail, gmap
    * api (HBase) but no sql interface.
    * slower than bigquery
    * indexed by a single Row Key
* Storage -- large immutable blobs
    * https://cloud.google.com/compute/docs/regions-zones/regions-zones
    * data lake, keep archive / unstructured data
    * temporary storage, like dataflow staging files.
* Dataflow
    * like spark
* Datastore / Firestore = BigTable + replication / transaction / secondary index
    * NoSQL database / schema-less
    * ACID
    * Smaller set of data
    * distributed / highly scalable
    * index / composite index: required for search
    * terms:
        * kind: class / DBMS table
        * properties: like table columns
        * entity: record
* Cloud Spanner:
    * initially based on mysql
    * replication configuration
    * globally-distributed database allowing consistent reads and writes.
    * -- not quite attractive
* Cloud SQL: hosted sql server
    * postgres
    * mysql
    * -- save some admin cost
* Stackdriver Logging

## Dataflow
* Pipelines
* PCollections
* Transforms
* I/O Sources and Sinks

## BigQuery: enterprise data warehouse, interactive query / OLAP
    project.dataset.table

## Container Cluster
* Identity and Access Management (IAM):
    * role based access control in Kubernetes
    * example: container.admin, container.developer

## Cloud SDK -- command line (gcloud, gsutil, and bq)

### Set up
```
gsdk_package=google-cloud-sdk-164.0.0-darwin-x86_64.tar.gz
wget https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/$gsdk_package && tar -xzf $gsdk_package
gcloud components update --quiet
```

### Cloud configurations
```
gcloud help
gcloud init
gcloud auth application-default login
gcloud auth activate-service-account <svc_xxx@d..gserviceaccount.com> --key-file=<...>

gcloud compute project-info add-metadata \
    --metadata google-compute-default-region=us-east1,google-compute-default-zone=us-east1-c
```

### Command line format
* gcloud GROUP | COMMAND ...
    * GROUP:  app | auth | components | compute | config | container | dataflow | dataproc | datastore | iam | ml-engine

### Commands
```
# (set / switch project)
gcloud config set project <gcp_project>

# (config and list)
gcloud auth list
gcloud config list
gcloud projects list
gcloud components list
gcloud components update <component-name>
gcloud compute instances list
gcloud service-management configs list --service=SERVICE
gcloud service-management list --enabled
```

### BigQuery

#### BigQuery Command Line
```
# (basic format)
bq.py [--global_flags] <command> [--command_flags] [args]

# (check)
bq show ...
bq show --project_id=<gcp_project> <dataset>.<table>
bq show --project_id=<gcp_project> --format prettyjson <dataset>.<table> > <table>.json
bq head -n 10 --project bigquery-public-data samples.shakespeare  # preview top 10 records

# (jobs)
bq ls --jobs  # --datasets|-d --projects|-p
bq cancel <job_id>
# (load data)
bq mk elements  ## make dataset
bq mk --schema <schema string or json file> -t <mydataset.newtable>
bq mk --time_partitioning_type=DAY --time_partitioning_expiration=<259200> <mydataset.newtable>  # set expiration date in seconds
bq load elements.ptable gs://my-periodictable-bucket/PeriodicTableDataSet.csv ptable_schema.json
bq load --autodetect test_sets.qa_listing_domains qa_listing_domains.csv

# (export data)
bq extract --format csv <dataset>.<table> gs://bigquery-extracted/extracted.csv

# (query)
bq query "SELECT * FROM [bigquery-public-data.samples.shakespeare] LIMIT 5;"
wildcard tables: select from `<project-id>.<dataset-id>.<table-prefix>*` where <bool_expression>

* external table
bq mk --external_table_definition=[DEFINITION_FILE] [DATASET_ID].[TABLE_NAME]
bq mkdef --autodetect --source_format=NEWLINE_DELIMITED_JSON 'gs://bucket/file.csv'

```

#### BigQuery Sql
```
# meta data
SELECT * FROM <dataset>.__TABLES__;
SELECT * FROM INFORMATION_SCHEMA.SCHEMATA;
```

### Cloud Sql
```
# unix sockets
cloud_sql_proxy -dir=$HOME/tmp/cloudsql -instances=<gcp_project>:us-central1:<db_host>
cloud_sql_proxy -dir=$HOME/tmp/cloudsql -instances=<gcp_project>:us-central1:url-cov-test

# tcp sockets: preferred if using saved password
cloud_sql_proxy -instances=<gcp_project>:us-central1:<db_host>=tcp:5432
cloud_sql_proxy -instances=<gcp_project>:us-central1:urlcoverage=tcp:5432

# client
gcloud sql connect urlcoverage --user=postgres
gcloud sql connect <db_host> --user=postgres

# unix socket client
psql "sslmode=disable host=$HOME/tmp/cloudsql/<gcp_project>:us-central1:<db_host> dbname=urlcov user=postgres "

# tcp socket client
## PGPASSFILE="~/.pgpass"
psql -w -h 127.0.0.1 -U postgres urlcov
```
### Storage
```
gsutil ls
gsutil ls gs://
gsutil du -s gs://

# (permissions)
gsutil -m acl ch -r -u <user or role>:R gs://<artifacts.your-project.appspot.com>

# (remove temp and sub directory)
gsutil mb gs://airflow-config  # create bucket
gsutil mb -c regional -l us-west1 gs://bigquery-extracted
gsutil cp *.txt gs://my-bucket
gsutil rm gs://<path to file>
gsutil rm -r gs://<directory>

# (give permissions)
gsutil acl ch -u user@gmail.com:W gs://my-awesome-bucket

# (remove temp and sub directory)
gsutil rm gs://analytic-staging/urlsummary/temp/**
```

### Dataflow
* gcloud dataflow jobs list

### Container Cluster
```
gcloud components install kubectl
# (create container)
gcloud container clusters create <airflow-cluster>
gcloud container clusters delete <cluster-test>
gcloud container clusters list

# (set default cluster)
gcloud container clusters get-credentials <airflow-cluster>
gcloud container clusters get-credentials airflow-cluster --zone us-central1-a --project=<gcp_project>
gcloud container clusters get-credentials operations2 --zone us-central1-a --project=<gcp_project>

# (image operations)
# tag, then push
image_id=14f60031763d && image_tag=ubuntu_16 && gcp_project=<gcp_project>
docker tag $image_id gcr.io/$gcp_project/$image_tag
gcloud docker -- push gcr.io/$gcp_project/$image_tag

# download image from container registry
gcloud docker -- pull us.gcr.io/<gcp_project>/airflow

gcloud container images list --repository=us.gcr.io/<gcp_project>
gcloud container images list --repository=us.gcr.io/<gcp_project>
gcloud container images list --repository=gcr.io/google_containers

gcloud container images delete appengine --repository=us.gcr.io/<gcp_project>

# (run / deploy image and expose services)
kubectl run airflow --image=us.gcr.io/<gcp_project>/airflow:20170718 --port=8080 --replicas=1
kubectl run airflow --image=gcr.io/<gcp_project>/airflow:20170718 --port=8080 --replicas=1
kubectl run airflow-postgres --image=us.gcr.io/<gcp_project>/airflow-postgres --port=5432 --replicas=1
kubectl run airflow-postgres --image=gcr.io/<gcp_project>/airflow-postgres --port=5432 --replicas=1

kubectl expose deployment airflow --type=LoadBalancer --port 8080
kubectl expose deployment airflow-postgres --type=ClusterIP --port 5432

image=$(kubectl get pods | grep "airflow" | awk '{print $1}')
kubectl exec -it $image bash

kubectl get service airflow
kubectl delete service airflow

kubectl get pods
kubectl delete pods --all

kubectl delete deployment airflow

kubectl proxy  # Starting to serve on 127.0.0.1:8001
```
### Compute Engine

### App Engine
```
gcloud app --project <gcp_project> instances enable-debug
```
