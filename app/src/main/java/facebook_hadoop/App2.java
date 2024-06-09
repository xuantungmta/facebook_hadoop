package facebook_hadoop;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.IOException;

import javax.naming.Context;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class App2 {
    public static class ReportMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);

        public void map(Object ikey, Text ivalue, Context context)
                throws IOException, InterruptedException {
            String row = ivalue.toString();
            String[] cells = row.split(",");
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                Date createdDate = df.parse(cells[4]);
                if (toDateRange(createdDate))
                    context.write(new Text(cells[0]), one);
            } catch (Exception ex) {
            }
        }

        private Boolean toDateRange(Date createdDate) {
            return createdDate.compareTo(new Date("20/5/2024")) >= 0
                    && createdDate.compareTo(new Date("31/06/2024")) < 0;
        }
    }

    public static class DataExtractionReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private final static IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "facebook post count");
        job.setJarByClass(App2.class);
        job.setMapperClass(ReportMapper.class);
        job.setReducerClass(DataExtractionReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
