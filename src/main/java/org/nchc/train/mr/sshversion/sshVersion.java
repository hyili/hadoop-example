package org.nchc.train.mr.sshversion;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * Hello world!
 *
 */
public class sshVersion 
{
    public static void main( String[] args ) throws Exception{
    	Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        
        if (otherArgs.length != 2) {
                System.err.println("Usage: sshversion  ");
                System.exit(2);
        }
        
        conf.set("mapreduce.output.textoutputformat.separator", ", ");
        Job job = Job.getInstance(conf, "ssh version");
        job.setJarByClass(sshVersion.class);
        job.setMapperClass(sshVersionMapper.class);
        job.setCombinerClass(sshVersionReducer.class);
        job.setReducerClass(sshVersionReducer.class);
        
        
        /**设置map和reduce函数的输入类型，这里没有代码是因为我们使用默认的TextInputFormat，
         * 针对文本文件，按行将文本文件切割成 InputSplits, 并用 LineRecordReader 
         * 将 InputSplit 解析成 <key,value> 对，key 是行在文件中的位置，value 是文件中的一行**/

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
