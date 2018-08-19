import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndexJob {

  public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private IntWritable docID = new IntWritable();
    private Text word = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

      String line = value.toString();
      StringTokenizer tokenizer = new StringTokenizer(line);

      // First Token is DocumentID
      docID.set(Integer.parseInt(tokenizer.nextToken()));

      while (tokenizer.hasMoreTokens()) {
        word.set(tokenizer.nextToken());
        context.write(word, docID);
      }
    }
  }

  public static class WordCountReducer extends Reducer<Text, IntWritable, Text, Text> {

    // Iterable<IntWritable> values are DocumentID's for a key (i.e. words)

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      Map<Integer, Integer> myMap = new HashMap<Integer, Integer>();

      for(IntWritable value : values) {
        int docID = value.get();

        if(myMap.containsKey(docID)) {
          myMap.put(docID, myMap.get(docID) + 1);
        } else {
          myMap.put(docID, 1);
        }
      }

      String result = myMap.toString();
      context.write(key, new Text(result));
    }
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

    if(args.length != 2) {
      System.err.println("Usage: Inverted Index <intput path> <output path>");
      System.exit(-1);
    }

    // Configuration conf = new Configuration();

    Job job = new Job();
    job.setJarByClass(InvertedIndexJob.class);
    job.setJobName("Inverted Index");

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.setMapperClass(WordCountMapper.class);
    job.setReducerClass(WordCountReducer.class);

    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    job.waitForCompletion(true);
  }
}