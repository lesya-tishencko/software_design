package ru.spbau.mit.sd;

import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.sd.command.*;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Vector;

public class CliTest {

    @Test
    public void testPreprocessor() {
        Environment env = new Environment();
        Preprocessor preprocessor = new Preprocessor();
        String var = "a=5 b=4 c=10 x=it";
        env.put("a", "4");
        env.put("b", "5");
        env.put("c", "10");
        env.put("x", "it");
        String line = "echo \"$b abc $b\" $a $b ex$x";
        String line_not_change = "echo \'$b $x\'";
        Assert.assertEquals(var, preprocessor.preprocess(env, var));
        Assert.assertEquals("echo \"5 abc 5\" 4 5 exit", preprocessor.preprocess(env, line));
        Assert.assertEquals(line_not_change, preprocessor.preprocess(env, line_not_change));
        line = "echo $e | cat $d | echo $f";
        env.put("e", "2");
        env.put("d", "5");
        Assert.assertEquals( "echo 2 | cat 5 | echo", preprocessor.preprocess(env, line));

    }

    @Test
    public void testParser() {
        Environment env = new Environment();
        Parser parser = new Parser();
        String line = "echo 123 543 | wc | cat test";
        parser.parse(line);
        Assert.assertEquals("echo", parser.getCommands().elementAt(0));
        Assert.assertEquals("wc", parser.getCommands().elementAt(1));
        Assert.assertEquals("cat", parser.getCommands().elementAt(2));
        Assert.assertEquals(new Vector<>(Arrays.asList(new String[]{"123 543", "", "test"})), parser.getParams());
    }

    @Test
    public void testWc() {
        Environment env = new Environment();
        Wc wc = new Wc();
        wc.execute(env, System.in, System.out, "src/test/resources/test1 src/test/resources/test2");
    }

    @Test
    public void testEcho() {
        Echo echo = new Echo();
        echo.execute(new Environment(), System.in, System.out, "it works");
    }

    @Test
    public void testCat() {
        Environment env = new Environment();
        Cat cat = new Cat();
        cat.execute(env, System.in, System.out, "src/test/resources/test1");
    }

    @Test
    public void testPwd() {
        Environment env = new Environment();
        env.put("PWD", System.getProperty("user.dir"));
        Pwd pwd = new Pwd();
        pwd.execute(env, System.in, System.out, "");
    }

    @Test
    public void testLs() {
        Environment env = new Environment();
        env.put("PWD", System.getProperty("user.dir"));
        Ls ls = new Ls();
        ls.execute(env, System.in, System.out, "\\");
        ls.execute(env, System.in, System.out, "..");
        ls.execute(env, System.in, System.out, "src/main/java/ru/spbau/mit/sd/command");

        ls.execute(env, System.in, System.out, "src/main/command");
    }

    @Test
    public void testCd() {
        Environment env = new Environment();
        env.put("PWD", System.getProperty("user.dir"));
        Cd cd = new Cd();
        String path = env.get("PWD");
        cd.execute(env, System.in, System.out, "\\");
        assertEquals(System.getProperty("user.home"), env.get("PWD"));
        cd.execute(env, System.in, System.out, path);
        assertEquals(path, env.get("PWD"));

        cd.execute(env, System.in, System.out, "src/main/java/ru/spbau/mit/sd/command");
        cd.execute(env, System.in, System.out, "../../../../../../../test");
        assertEquals(path + "\\src\\test", env.get("PWD"));
        cd.execute(env, System.in, System.out, path);
    }
}