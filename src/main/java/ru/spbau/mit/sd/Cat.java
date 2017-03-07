package ru.spbau.mit.sd;
import java.io.*;

class Cat extends Factory {

    /**
     * Override method in class Factory.
     * if @param.length == 0 then read from ios and write all data to ios
     * otherwise read data from file which names take as parameters (param)
     * and write data to ios
     * @param ios IOStream object for write and read
     * @param param parameters of command cat
     */
    @Override
    public void execute(IOStream ios, String param) {
        byte[] buf = new byte[ios.bufSize()];
        if (param.length() == 0) {
            try {
                int temp = ios.read(buf);
                ios.write(buf, 0, temp);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            File file;
            int off = 0;
            for (String arg : param.trim().split("\\s+")) {
                try {
                    file = new File(arg);
                    int len = 0;
                    len = ios.readFile(file, buf);
                    ios.write(buf, off, len);
                    ios.write(System.lineSeparator().getBytes());
                    off = len + 1;
                } catch (FileIsDirectoryException e) {
                    ios.write(("cat: " + arg + ": Is a directory"
                            + System.lineSeparator()).getBytes());
                } catch (FileNotFoundException e) {
                    ios.write(("cat: " + arg + ": No such file or directory"
                            + System.lineSeparator()).getBytes());

                }
            }
        }
        buf = null;
    }
}

