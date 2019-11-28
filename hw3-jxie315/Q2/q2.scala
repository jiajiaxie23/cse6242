// Databricks notebook source
// Q2 [25 pts]: Analyzing a Large Graph with Spark/Scala on Databricks

// STARTER CODE - DO NOT EDIT THIS CELL
import org.apache.spark.sql.functions.desc
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import spark.implicits._

// COMMAND ----------

// STARTER CODE - DO NOT EDIT THIS CELL
// Definfing the data schema
val customSchema = StructType(Array(StructField("answerer", IntegerType, true), StructField("questioner", IntegerType, true),
    StructField("timestamp", LongType, true)))

// COMMAND ----------

// STARTER CODE - YOU CAN LOAD ANY FILE WITH A SIMILAR SYNTAX.
// MAKE SURE THAT YOU REPLACE THE examplegraph.csv WITH THE mathoverflow.csv FILE BEFORE SUBMISSION.
val df = spark.read
   .format("com.databricks.spark.csv")
   .option("header", "false") // Use first line of all files as header
   .option("nullValue", "null")
   .schema(customSchema)
   .load("/FileStore/tables/mathoverflow.csv")
   .withColumn("date", from_unixtime($"timestamp"))
   .drop($"timestamp")

// COMMAND ----------

//display(df)
df.show()

// COMMAND ----------

// PART 1: Remove the pairs where the questioner and the answerer are the same person.
// ALL THE SUBSEQUENT OPERATIONS MUST BE PERFORMED ON THIS FILTERED DATA

// ENTER THE CODE BELOW
val q1=df.filter("answerer != questioner")
q1.show()

// COMMAND ----------

// PART 2: The top-3 individuals who answered the most number of questions - sorted in descending order - if tie, the one with lower node-id gets listed first : the nodes with the highest out-degrees.

// ENTER THE CODE BELOW
val p2= df.filter("answerer != questioner").orderBy("answerer").groupBy("answerer").count().orderBy(desc("count")).withColumnRenamed("count", "questions_answered")
p2.show(3)


// COMMAND ----------

// PART 3: The top-3 individuals who asked the most number of questions - sorted in descending order - if tie, the one with lower node-id gets listed first : the nodes with the highest in-degree.

// ENTER THE CODE BELOW
val p3= df.filter("answerer != questioner").orderBy("questioner").groupBy("questioner").count().orderBy(desc("count")).withColumnRenamed("count", "questions_asked")
p3.show(3)

// COMMAND ----------

// PART 4: The top-5 most common asker-answerer pairs - sorted in descending order - if tie, the one with lower value node-id in the first column (u->v edge, u value) gets listed first.

// ENTER THE CODE BELOW
val q4 =df.filter("answerer != questioner").groupBy("answerer", "questioner").count().orderBy(desc("count"))
q4.show(5)

// COMMAND ----------

// PART 5: Number of interactions (questions asked/answered) over the months of September-2010 to December-2010 (i.e. from September 1, 2010 to December 31, 2010). List the entries by month from September to December.

// Reference: https://www.obstkel.com/blog/spark-sql-date-functions
// Read in the data and extract the month and year from the date column.
// Hint: Check how we extracted the date from the timestamp.

// ENTER THE CODE BELOW
val q5 = df.filter("answerer != questioner").filter(df("date") <=(lit("2010-12-31"))).filter(df("date") >=(lit("2010-09-01")))
.groupBy(month(df.col("Date")).alias("month")).count().withColumnRenamed("count","total_interactions")
q5.show()

// COMMAND ----------

// PART 6: List the top-3 individuals with the maximum overall activity, i.e. total questions asked and questions answered.

// ENTER THE CODE BELOW
val first = df.filter("answerer != questioner").groupBy("answerer").count().withColumnRenamed("count", "value1").withColumnRenamed("answerer","1id")


val second = df.filter("answerer != questioner").groupBy("questioner").count().withColumnRenamed("count","value2").withColumnRenamed("questioner","1id")

val tmp=second.join(first, first("1id")===second("1id"), "outer" ).withColumn("userID", coalesce(first("1id"),second("1id"))).drop("1id")
val tmp2=tmp.na.fill(0.0, Seq("value2","value1"))
val tmp3 = tmp2.withColumn("total_activity", tmp2("value1") + tmp2("value2")).drop("value1").drop("value2").
orderBy(desc("total_activity")).show(3)



