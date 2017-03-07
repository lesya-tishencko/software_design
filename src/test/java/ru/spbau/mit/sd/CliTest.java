package ru.spbau.mit.sd;

import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class CliTest {

    private void print(IOStream ios) {
        final int bufSize = 1024;
        byte[] buf = new byte[bufSize];
        try {
            int size = ios.read(buf);
            while (size != -1) {
                System.out.write(buf, 0, size);
                if (size < bufSize) {
                    break;
                }
                size = ios.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPreprocessor() {
        Preprocessor preprocessor = new Preprocessor();
        String var = "a=5 b=4 c=10 d=5 e=2 x=it";
        String line = "echo \"$b abc $b\" $a $b ex$x";
        Assert.assertEquals(preprocessor.preprocess(var), "");
        Assert.assertEquals(preprocessor.preprocess(line), "echo \"4 abc 4\" 5 4 exit");
        line = "echo $e | cat $d | echo $f";
        Assert.assertEquals(preprocessor.preprocess(line), "echo 2 | cat 5 | echo");

    }

    @Test
    public void testParser() {
        Parser parser = new Parser();
        String line = "echo 123 543 | wc | cat test";
        parser.parse(line);
        Assert.assertEquals(parser.getCommands().elementAt(0), "echo");
        Assert.assertEquals(parser.getCommands().elementAt(1), "wc");
        Assert.assertEquals(parser.getCommands().elementAt(2), "cat");
        Assert.assertEquals(parser.getParams(), new Vector<>(Arrays.asList(new String[]{"123 543", "", "test"})));
    }

    @Test
    public void testWc() {
        Wc wc = new Wc();
        IOStream ios = new IOStream();
        wc.execute(ios, "test/test1 test/test2 test/test3");
        print(ios);
    }

    @Test
    public void testEcho() {
        Echo echo = new Echo();
        IOStream ios = new IOStream();
        print(ios);
    }

    @Test
    public void testIOStream() {
        IOStream ios = new IOStream();
        final int bufSize = 1024;
        byte[] buf = new byte[bufSize];
        try {
            ios.write("hello world!".getBytes());
            Assert.assertEquals(ios.read(buf), "hello world!".length());
            ios.write(("first line" + System.lineSeparator() + "second line").getBytes());
            ios.readFile(new File("test/test.pdf"), buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCat() {
        Cat cat = new Cat();
        IOStream ios = new IOStream();
        cat.execute(ios, "test/test1 test/test2");
        print(ios);
    }

    @Test
    public void testPwd() {
        Pwd pwd = new Pwd();
        pwd.execute(new IOStream(), "");
    }

}