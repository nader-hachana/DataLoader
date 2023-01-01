# 1. Environment installations

- ## Download sbt 1.2.8
Download sbt 1.2.8 on your machine then run the following command inside the project path folder to build the application jar file
>sbt clean assembly 

- ## Download Docker Desktop

  - ## Pull Cassandra image (version 4.0) from dockerhub 
  I used bitnami/cassandra:4.0 because When the container is executed for the first time, it will execute the files with extensions .sh, .cql or .cql.gz located at /docker-entrypoint-initdb.d in sort'ed order by filename. This way it becomes easier to initialize the database when it's created.
  >docker pull bitnami/cassandra:4.0

  - ## Pull Apache Spark image (version 3.2.1-hadoop-2.7) from dockerhub
  >docker pull apache/spark:v3.2.1

  - ## Create the docker network to perform containers communication
  The default driver is "bridge".
  >docker network create dataloader

# 2. Running the project *locally* 

- ## Create Cassandra container and run it: 
It will initialize the database with the keyspaces and the tables mentioned within /docker-entrypoint-initdb.d/schema.cql .
>docker-compose -f db.yml run -d

- ## Load the data inside the cassandra database
*Warn*: If you faced an error whilst loading metadata, just run "rm  ~/.docker/config.json".
>docker-compose -f load.yml build load  
>docker-compose -f load.yml run load

# 3. Running the project *on Airflow*

- ## Download Airflow
This requires Python version 3.9.x to be compatible with the constraints mentioned below.
>sudo pip install "apache-airflow==2.3.1" --constraint "https://raw.githubusercontent.com/apache/airflow/constraints-2.3.1/constraints-3.9.txt"

- ## Configure the airflow home variable
>export AIRFLOW_HOME=~/airflow

- ## Run Airflow server
>airflow standalone

- ## Move python file to airflow/dags path for each modification made on the file
*Warn*: before moving the data_loader python file to the dags folder you need to change the content of { cwd = '/.../' } within the code to **your project directory**.
>sudo cp data_loader.py ~/airflow/dags

- ## Run the dag on the airflow UI or through CLI
The "-v or --verbose" has been introduced to airflow on the version 2.5.0 .
>airflow dags trigger  [-v or --verbose] data_loader

- ## Visualize the result on the terminal
>airflow dags show data_loader

- ## Save the Dag graph as an image on the local directory
>sudo apt-get install graphviz  
>airflow dags show data_loader --save data_loader.png









