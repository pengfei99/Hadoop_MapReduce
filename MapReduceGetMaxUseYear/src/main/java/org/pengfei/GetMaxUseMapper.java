package org.pengfei;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class GetMaxUseMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line=value.toString();
        StringTokenizer st = new StringTokenizer(line,"\t");
        String year = st.nextToken();
        String lastToken = null;
        while(st.hasMoreTokens()){
            lastToken=st.nextToken();
        }
        int avgPrice=Integer.parseInt(lastToken);
        context.write(new Text(year),new IntWritable(avgPrice));
    }
}
