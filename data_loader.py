# import the libraries

from datetime import timedelta
from airflow import DAG
from airflow.operators.bash_operator import BashOperator
from airflow.utils.dates import days_ago


#defining DAG arguments

default_args = {
    'owner': 'Nader Hachana',
    'start_date': days_ago(0),
    'email': ['naderhachana96@gmail.com'],
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 0,
    'retry_delay': timedelta(minutes=5),
}

# define the DAG    
dag = DAG(
    dag_id='data_loader',
    default_args=default_args,
    description='data loader',
    schedule_interval=timedelta(days=1),
)

# define the tasks

# This task is basically to make sure that we are working on the current directory
ls = BashOperator(
    task_id='ls',
    bash_command='ls',
    dag=dag,
    cwd='/mnt/c/Users/Nader Hachana/OneDrive/Documents/Projects/Dataloader_etl' 
)

create_db = BashOperator(
    task_id='create_db',
    bash_command='docker-compose -f db.yml up -d; \
                  sleep 60',
    dag=dag,
    cwd='/mnt/c/Users/Nader Hachana/OneDrive/Documents/Projects/Dataloader_etl'
    # the cwd option is to set the working directory you would like for the command to be executed in. 
                   # If the option isn't passed, the command is executed in a temporary directory.
                   # It was added in Airflow 2.2
)

load_to_db = BashOperator(
    task_id='load_to_db',
    bash_command='docker-compose -f load.yml build load;\
                  docker-compose -f load.yml run load',
    dag=dag,
    cwd='/mnt/c/Users/Nader Hachana/OneDrive/Documents/Projects/Dataloader_etl' 
)


# task pipeline
ls >> create_db >> load_to_db 