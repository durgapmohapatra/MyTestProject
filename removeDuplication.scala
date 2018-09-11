import org.apache.spark.SparkContext
import scala.collection.JavaConverters._
import org.chombo.stats.CompleteStat
import org.chombo.util.BasicUtils

object DuplicateRemover extends JobConfiguration {
   

   def main(args: Array[String]) {
	   val appName = "duplicateRemover"
	   val Array(inputPath: String, outputPath: String, configFile: String) = getCommandLineArgs(args, 3)
	   val config = createConfig(configFile)
	   val sparkConf = createSparkConf(appName, config, false)
	   val sparkCntxt = new SparkContext(sparkConf)
	   val appConfig = config.getConfig(appName)
	   
	   //configuration params
	   val fieldDelimIn = appConfig.getString("field.delim.in")
	   val fieldDelimOut = appConfig.getString("field.delim.out")
	   val keyAttributes = getOptionalIntListParam(appConfig, "key.field.ordinals")
	   val KeyAttributeArray = keyAttributes match {
	     case Some(ords : java.util.List[Integer]) => Some(BasicUtils.fromListToIntArray(ords))
	     case None => None
	   }
	   val outpputMode = getStringParamOrElse(appConfig, "output.mode", "all")
	   val outputWholeRec = this.getBooleanParamOrElse(appConfig, "output.wholeRec", true)
	   val dupRecsFilePath = getOptionalStringParam(appConfig, "dup.recs.file.path")
	   val debugOn = appConfig.getBoolean("debug.on")
	   val saveOutput = appConfig.getBoolean("save.output")
	   
	   //accumulators
	   val dupRecCount = sparkCntxt.accumulator[Long](0, "dupRecCount")

	   
	   val data = sparkCntxt.textFile(inputPath)
	   val keyedData = data.keyBy(r => {
	     val key = KeyAttributeArray match {
	       case Some(ords : Array[Int]) => {
	         val items = r.split(fieldDelimIn, -1)
	         BasicUtils.extractFields(items, ords, fieldDelimIn)}
	       case None => r
	     }
	     key
	   })

	   //dup removed
	   val groupedData = keyedData.groupByKey
	   groupedData.cache
	   val dupRemData = groupedData.mapValues(vi => {
	     val va = vi.toArray
	     var rec = va.head
	     val size = va.length
	     dupRecCount += (size - 1)
	     rec = outpputMode match {
	       case "all" => rec
	       case "duplicate" => if (size > 1) rec else "X"
	       case "unique" => if (size == 1) rec else "X"
	     }
	     rec
	   }).filter(v => !v._2.equals("X"))
	   
	   val outputData = dupRemData.map(v => {
	     if (outputWholeRec) v._2 else v._1
	   })
	   
	   if (debugOn) {
         val records = outputData.collect
         records.foreach(r => println(r))
         println("duplicate record count:" + dupRecCount.value)
         
       }
	   
	   if(saveOutput) {	   
	     outputData.saveAsTextFile(outputPath) 
	     
	     //optinally save duplicate records
	     dupRecsFilePath match {
		   case Some(path : String) => {
			   val dupRecords = groupedData.flatMapValues(vi => {
				   val va = vi.toArray
	               val size = va.length
	               val uniqueRec = List("X")
	               if (size > 1) vi else uniqueRec
			   }).filter(v => !v._2.equals("X"))
		       dupRecords.saveAsTextFile(path) 	   
		   }
		   case None => 
		 }

	   }

   }
   
   
}
