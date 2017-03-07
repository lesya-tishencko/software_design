package ru.spbau.mit.sd;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

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
        while (true) {
            System.out.println("input new command:");
            String line = scanner.nextLine();
            line = preprocessor.preprocess(line);
            parser.parse(line);
            Vector<String> commands = parser.getCommands();
            Vector<String> params = parser.getParams();
            IOStream ios = new IOStream();
            Factory command;
            for (int i = 0; i < commands.size(); ++i) {
                command = Factory.getCommand(commands.elementAt(i));
                command.execute(ios, params.elementAt(i));
            }
            try {
                int size = ios.read(buf);
                while (size != -1) {
                    System.out.write(buf, 0, size);
                    if (size < BUF_SIZE) {
                        break;
                    }
                    size = ios.read(buf);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

