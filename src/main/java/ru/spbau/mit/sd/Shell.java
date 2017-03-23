package ru.spbau.mit.sd;

import ru.spbau.mit.sd.command.CommandException;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

/**
 * main class to run shell
 */
public class Shell {
    /**
     * run command line interpreter
     */
    public static void main(String[] args) {
        new Shell().run();
    }

    private void run() {
        final int BUF_SIZE = 1024;
        byte[] buf = new byte[BUF_SIZE];

        Scanner scanner = new Scanner(System.in);
        Preprocessor preprocessor = new Preprocessor();
        Parser parser = new Parser();
        Environment env = new Environment();
        String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        env.put("PWD", absolutePath);
        boolean END = false;

        while (!END) {
            System.out.println("input new command:");
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = preprocessor.preprocess(env, line);
                parser.parse(line);
                Vector<String> commands = parser.getCommands();
                Vector<String> params = parser.getParams();
                int[] pipePos = parser.getPipePos();
                PipedInputStream pis = new PipedInputStream(4096);
                PipedOutputStream pos = new PipedOutputStream();
                try {
                    pos.connect(pis);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Runnable command;
                int j = 0;
                InputStream inputStream = System.in;
                OutputStream outputStream = System.out;
                for (int i = 0; i < commands.size(); ++i) {
                    command = Runnable.getCommand(commands.elementAt(i));
                    if (i >= pipePos[j]) {
                        ++j;
                        pipePos[j] += pipePos[j - 1];
                        inputStream = pis;
                    }
                    if (i == pipePos[j] - 1) {
                        if (j == pipePos.length - 1) {
                            outputStream = System.out;
                        } else {
                            outputStream = pos;
                        }
                    }
                    try {
                        command.execute(env, inputStream, outputStream, params.elementAt(i));
                    } catch (CommandException e) {
                        break;
                    }
                }

            } else {
                System.out.println("good bye!");
                END = true;
            }
        }
    }

}

