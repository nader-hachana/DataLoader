 /opt/spark/bin/spark-submit \
    --master local[*] \
    --deploy-mode client \
    --class "com.cognira.DataLoad.Load" \
    --conf spark.cassandra.auth.username=cassandra \
    --conf spark.cassandra.auth.password=cassandra \
    target/scala-2.12/dataloader_2.12-0.1.jar