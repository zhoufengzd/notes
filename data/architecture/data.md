# Data Notes
* Data lake: store everything, even those not well defined.
* Data warehouses: column storage solutions

## GCP: Cloud Storage as a data lake
* Cost efficiency
* Flexible processing:
    * BigQuery: relatively simple ETL
    * Dataflow: serverless / complex operations
* Security / Strong consistency

## AWS: S3
*

## Redshift vs BigQuery
* Amazon Redshift is provisioned on clusters and nodes. Google BigQuery is serverless.
* Redshift requires periodic management tasks like vacuuming tables, BigQuery has automatic management.
* Redshift can rollback (built on PostgreSql)

## OLTP vs OLAP
* OLTP: Online Transaction Processing (integrity)
* OLAP: Online Analytic Processing
