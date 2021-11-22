# Airflow Notes

## Key Concept
* dag: base_dag
    * dag_id, task_ids
* hooks
    * an interface / connection to external systems, e.g., MySqlHook.
* operators
* executors

## command line
```
-- db reset / initdb / upgrade
-- pause, unpause
-- list_dags, list_tasks, task_state, dag_state
-- test, run, trigger_dag, backfill, clear
airflow list_dags
airflow unpause <dag_id>
airflow pause <dag_id>
```

## dag structure
```
dag
  |__ analytics
        |__ dag_x.yml, dag_y.yml
  |__ sql
        |__ init
        |__ views
        |__ step1.sql, ..., stepN.sql
  |__ driver.py: build dags
```

## Dags
```python
class BaseDag(object):
    def dag_id(self):
    def task_ids(self):

class BaseDagBag(object):
    def dag_ids(self):

class DagBag(BaseDagBag):
    """a collection of dags, parsed out of a folder tree"""
    def __init__(
            self,
            dag_folder=None,
            executor=DEFAULT_EXECUTOR,
            include_examples=configuration.getboolean('core', 'LOAD_EXAMPLES')):
        self.collect_dags(dag_folder)
    def process_file(self, filepath, only_if_updated=True, safe_mode=True):
    def collect_dags(...):
        for root, dirs, files in os.walk(dag_folder, followlinks=True):
            self.process_file(...)
```

### models.py
```python
class DagBag(BaseDagBag):

class User(Base):

class Connection(Base):

class TaskInstance(Base):

class BaseOperator(object):

class Chart(Base):

class Variable(Base):

class XCom(Base):
    """operator cross-communication"""

class DagStat(Base):

class DagRun(Base):

class Pool(Base):
```

## Hooks
### Base Hook
```python
class BaseHook(object):
    get_connections(cls, conn_id):
    # conn_id: in the config database or defined env variable as AIRFLOW_CONN_xxx
```

### DB Hook
```
class DbApiHook(BaseHook):
    def get_conn(self):
        db = self.get_connection(getattr(self, self.conn_name_attr))
        return self.connector.connect(
            host=db.host,
            port=db.port,
            username=db.login,
            schema=db.schema)
    def get_records(self, sql, parameters=None):
    def run(self, sql, autocommit=False, parameters=None):
    def insert_rows(self, table, rows, target_fields=None, commit_every=1000):
```

### GCloud Hooks
* cloud client
* authentication:
    * default: only need project id
    * json key file: 'Project Id', 'Key Path' and 'Scope'.

#### GoogleCloudBaseHook
```python
class GoogleCloudBaseHook(BaseHook):
    def _authorize(self):
        if not key_path:
            # expects project id defined in airflow connection table
            credentials = GoogleCredentials.get_application_default()
        else
            # expects "Project Id", "Key Path", and "Scope"
            credentials = ServiceAccountCredentials.from_json_keyfile_name(key_path, scopes)

        http = httplib2.Http()
        return credentials.authorize(http)
```

#### BigQueryHook
```python
class BigQueryHook(GoogleCloudBaseHook, DbApiHook):
    def get_conn(self):
    def get_service(self):
        http_authorized = self._authorize()
        return apiclient.discovery.build('bigquery', 'v2', http=http_authorized)

    def get_pandas_df(self, bql, parameters=None, dialect='legacy'):
        """Returns a Pandas DataFrame for the results produced by a BigQuery"""
```

#### Dataproc hook
##### _DataProcJob
```python
class _DataProcJob:
    def __init__(self, dataproc_api, project_id, job):
        self.job = dataproc_api.projects().regions().jobs().submit(
            projectId=project_id,
            region='global',
            body=job).execute()
```

##### _DataProcJobBuilder
```python
class _DataProcJobBuilder:
    def __init__(self, project_id, task_id, dataproc_cluster, job_type, properties):
        name = task_id + "_" + str(uuid.uuid1())[:8]
        self.job = {
            "job": {
                "reference": {"projectId": project_id, "jobId": name,},
                "placement": {"clusterName": dataproc_cluster}
            }
        }

    def add_variables(self, variables):
        self.job["job"][self.job_type]["scriptVariables"] = variables

    def add_args(self, args):
        self.job["job"][self.job_type]["args"] = args

    def add_query(self, query):
        self.job["job"][self.job_type]["queryList"] = {'queries': [query]}
```

##### DataProcHook
```python
class DataProcHook(GoogleCloudBaseHook):
    def get_conn(self):
        http_authorized = self._authorize()
        return apiclient.discovery.build('dataproc', 'v1', http=http_authorized)

    def submit(self, project_id, job):
        submitted = _DataProcJob(self.get_conn(), project_id, job)
```

## Operators
### BaseOperator
```python
class BaseOperator(object):
    def dag(self, dag):
    def dag_id(self):
    def pre_execute(self, context):
    def execute(self, context):
    def post_execute(self, context, result=None):
    def on_kill(self):
    def run(self, start_date=None, end_date=None, ignore_first_depends_on_past=False, ignore_ti_state=False, mark_success=False):
        for dt in self.dag.date_range(start_date, end_date=end_date):
            TaskInstance(self, dt).run(...)
```

### BigQuery

#### BigQueryOperator
```python
class BigQueryOperator(BaseOperator):
    """Executes BigQuery SQL queries in a specific BigQuery database"""
    def execute(self, context):
        logging.info('Executing: %s', self.bql)
        hook = BigQueryHook(bigquery_conn_id=self.bigquery_conn_id,
                            delegate_to=self.delegate_to)
        conn = hook.get_conn()
        cursor = conn.cursor()
        cursor.run_query(self.bql, self.destination_dataset_table, self.write_disposition,
                         self.allow_large_results, self.udf_config, self.use_legacy_sql)
```

#### BigQueryToBigQueryOperator
```python
class BigQueryToBigQueryOperator(BaseOperator):
    """Copies data from one BigQuery table to another. """
    def execute(self, context):
        hook = BigQueryHook(bigquery_conn_id=self.bigquery_conn_id,
                            delegate_to=self.delegate_to)
        conn = hook.get_conn()
        cursor = conn.cursor()
        cursor.run_copy(
            self.source_project_dataset_tables,
            self.destination_project_dataset_table,
            self.write_disposition,
            self.create_disposition)
```

###
```python
```

## Executors
### BaseExecutor
```python
class BaseExecutor(LoggingMixin):
    def start(self):  # pragma: no cover
    def queue_command(self, task_instance, command, priority=1, queue=None):
        self.queued_tasks[key] = (command, priority, queue, task_instance)
    def queue_task_instance():
    def sync(self):
    def heartbeat(self):
```

### SequentialExecutor
```python
class SequentialExecutor(BaseExecutor):
    def sync(self):
        for key, command in self.commands_to_run:
            subprocess.check_call(command, shell=True)
```


## Airflow database
### Tables

### Check
```
    update dag set is_paused = false where dag_id = 'url_summary_1w';

drop procedure clean_dag;
delimiter //
create procedure clean_dag(dagid varchar(64), action varchar(16), scope varchar(8))
begin
    set @dag_id=dagid;
    if scope = 'all' then
        set @dag_stats_state = null;
    else
        set @dag_stats_state = 'success';
    end if;
    if action = 'run' then
        delete from log where dag_id = @dag_id;
        delete from xcom where dag_id = @dag_id;
        delete from sla_miss where dag_id = @dag_id;
        delete from task_fail where dag_id = @dag_id;
        delete from task_instance where dag_id = @dag_id;
        delete from job where dag_id = @dag_id;
        delete from dag_run where dag_id = @dag_id;
        delete from dag_stats where dag_id = @dag_id and (@dag_stats_state is null or state <> @dag_stats_state);
        delete from dag where dag_id = @dag_id;
    else
        select 'log' as table_name, @dag_id as dag_id, count(1) as count from log where dag_id = @dag_id
        union all select 'xcom', @dag_id, count(1) from xcom where dag_id = @dag_id
        union all select 'sla_miss', @dag_id, count(1) from sla_miss where dag_id = @dag_id
        union all select 'task_fail', @dag_id, count(1) from task_fail where dag_id = @dag_id
        union all select 'task_instance', @dag_id, count(1) from task_instance where dag_id = @dag_id
        union all select 'job', @dag_id, count(1) from job where dag_id = @dag_id
        union all select 'dag_run', @dag_id, count(1) from dag_run where dag_id = @dag_id
        union all select 'dag_stats', @dag_id, count(1) from dag_stats where dag_id = @dag_id
        union all select 'dag', @dag_id, count(1) from dag where dag_id = @dag_id;
    end if;
end //
delimiter ;

set @dag_id='vuln_trend';
call clean_dag(@dag_id, 'check', null);
call clean_dag(@dag_id, 'run', null); -- keep previous succeeded log
call clean_dag(@dag_id, 'run', 'all'); -- remove all log
```
