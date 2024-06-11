package facebook_hadoop;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class PostByTime {
    public static Date startDate;
    public static Date endDate;

    public static class ReportMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);

        public void map(Object ikey, Text ivalue, Context context)
                throws IOException, InterruptedException {
            String row = ivalue.toString();
            String[] cells = row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                Date createdDate = df.parse(cells[4]);
                if (toDateRange(createdDate))
                    context.write(new Text(cells[0]), one);
            } catch (Exception ex) {
                String mess = ex.getMessage();
            }
        }

        private Boolean toDateRange(Date createdDate) {
            return createdDate.compareTo(startDate) >= 0
                    && createdDate.compareTo(endDate) < 0;
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
        // initialize
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        startDate = df.parse(args[2]);
        endDate = df.parse(args[3]);

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "facebook post count");
        job.setJarByClass(PostByTime.class);
        job.setMapperClass(ReportMapper.class);
        job.setReducerClass(DataExtractionReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
