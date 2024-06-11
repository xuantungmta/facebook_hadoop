package facebook_hadoop;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class BankExtraction {
    public static class DataExtractionMapper extends Mapper<Object, Text, Text, Text> {
        public void map(Object ikey, Text ivalue, Context context)
                throws IOException, InterruptedException {
            String row = ivalue.toString();
            String[] cells = row.split(",");
            try {
                String incomeType = cells[5];
                float incomeTotal = Float.parseFloat(cells[6]);
                String hasCar = cells[3];
                String hasPhone = cells[4];
                boolean condition = incomeType.toLowerCase().equals("businessman") && incomeTotal >= 300000
                        && hasCar.toLowerCase().equals("y")
                        && hasPhone.equals("1");
                if (condition) {
                    Text key = new Text("1");
                    Text value = ivalue;
                    context.write(key, value);
                }

            } catch (Exception ex) {
            }
        }
    }

    public static class DataExtractionReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text val : values) {
                context.write(null, val);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "bank extraction");
        job.setJarByClass(BankExtraction.class);
        job.setMapperClass(DataExtractionMapper.class);
        job.setReducerClass(DataExtractionReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}