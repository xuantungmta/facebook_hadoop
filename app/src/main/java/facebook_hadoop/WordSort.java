package facebook_hadoop;

import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordSort {
    public static class DataExtractionMapper extends Mapper<Object, Text, IntWritable, Text> {

        public void map(Object ikey, Text ivalue, Context context)
                throws IOException, InterruptedException {
            try {
                String line = ivalue.toString();
                String[] tokens = line.split("\t");
                int valuePart = Integer.parseInt(tokens[1]);
                context.write(new IntWritable(valuePart), new Text(tokens[0]));
            } catch (Exception ex) {
                String mess = ex.getMessage();
            }
        }
    }

    public static class DataExtractionReducer extends Reducer<IntWritable, Text, Text, IntWritable> {
        public void reduce(IntWritable key, Iterable<Text> list, Context context)
                throws IOException, InterruptedException {
            for (Text value : list) {
                context.write(value, key);
            }
        }
    }

    public static class DescendingKeyComparator extends WritableComparator {
        protected DescendingKeyComparator() {
            super(IntWritable.class, true);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public int compare(WritableComparable w1, WritableComparable w2) {
            IntWritable key1 = (IntWritable) w1;
            IntWritable key2 = (IntWritable) w2;
            return -1 * key1.compareTo(key2);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Facebook statistical");
        job.setJarByClass(WordSort.class);
        job.setMapperClass(DataExtractionMapper.class);
        job.setReducerClass(DataExtractionReducer.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        job.setSortComparatorClass(DescendingKeyComparator.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        job.setInputFormatClass(TextInputFormat.class);

        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setOutputFormatClass(TextOutputFormat.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
