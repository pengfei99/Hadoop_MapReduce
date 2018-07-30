package org.pengfei;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class GetMaxUse extends Configured implements Tool {
    public static void main(String[] args)throws Exception{
        int exitCode = ToolRunner.run(new GetMaxUse(),args);
        System.exit(exitCode);

    }

    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Job job = Job.getInstance();
        job.setJarByClass(GetMaxUse.class);
        job.setJobName("GetMaxUseYear");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setMapperClass(GetMaxUseMapper.class);
        job.setReducerClass(GetMaxUseReducer.class);

        int returnValue = job.waitForCompletion(true) ? 0:1;
        System.out.println("job.isSuccessful " + job.isSuccessful());
        return returnValue;
    }

}
