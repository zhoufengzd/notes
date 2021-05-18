# Data Notes
* Data lake: store everything, even those not well defined.
* Data warehouses: column storage solutions
* Kappa Architecture: streaming -> kafka -> process

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

## OLTP vs OLAP
* OLTP: Online Transaction Processing (integrity)
* OLAP: Online Analytic Processing

## Kappa Architecture:
* same tech stack for both streaming and batch
* streaming -> kafka -> process
