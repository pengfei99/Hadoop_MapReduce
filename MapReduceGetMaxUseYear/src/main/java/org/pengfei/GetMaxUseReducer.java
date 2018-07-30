package org.pengfei;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class GetMaxUseReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int maxavg=30;
        int val=Integer.MIN_VALUE;
        Iterator<IntWritable> valuesIt = values.iterator();

        while (valuesIt.hasNext())
        {
            if((val=valuesIt.next().get())>maxavg)
            {
                context.write(key, new IntWritable(val));
            }
        }
    }
}
