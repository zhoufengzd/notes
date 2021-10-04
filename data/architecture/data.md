# Data Notes
* Data lake: store everything, even those not well defined.
* Data warehouses: column storage solutions
* Kappa Architecture: streaming -> kafka -> process

## OLTP vs OLAP
* OLTP: Online Transaction Processing (integrity)
* OLAP: Online Analytic Processing

### Change-Data-Capture (CDC)
* DB binlog => Kafka => BigQuery / RedShift
* Use db read replica
* Benefit:
    * Maintain application performance
    * Analyze historical data / Track delete as well
    * Faster analytics.
    * Scalability

## Kappa Architecture:
* same tech stack for both streaming and batch
* streaming -> kafka -> process

## lambda Architecture:
* prepare the data before queries
* batch:
    * Batch Layer: T0, T1,.., Tn-1, Tn
    * Tools: hadoop
    * Server layer: indexing / fix coding error
* streaming:
    * Speed layer: only latest data Tn-1, Tn.
    * Tools: Apache storm, Spark
* query: consume the data from batch or streaming

## Edge Computing
* Computing at the “edge” of a network
* trimming fat at the source
* pro: quick decisions + cost reduction

### Components
* Devices.
* Edge server or gateway
* Edge network or micro data center

## GCP: Cloud Storage as a data lake
* Cost efficiency
* Flexible processing:
    * BigQuery: relatively simple ETL
    * Dataflow: serverless / complex operations
* Security / Strong consistency

## AWS: S3
*

## Redshift vs BigQuery
* Architecture:
    * Redshift: clusters and nodes.
        * leader nodes / compute nodes
        * define # of nodes in configuration
    * BigQuery is serverless.
* Redshift requires periodic management tasks like vacuuming tables, BigQuery has automatic management.
* Redshift can rollback (built on PostgreSql)

## AWS: Redshift vs Athena
* Athena: serverless ansi sql over S3
* UDF: only on Redshift
