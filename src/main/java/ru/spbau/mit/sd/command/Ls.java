package ru.spbau.mit.sd.command;

import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * class implements Runnable and shows list of files in current directory.
 */
public class Ls implements Runnable {
    /**
     * Implement method in Runnable.
     * if @param.length == 0 then shows files in current directory
     * otherwise shows files in director which name take as parameter (param)
     * @param env - Environment
     * @param inputStream - stream to read data
     * @param outputStream - stream to write result
     * @param param parameters of command cat
     */
    @Override
    public void execute(Environment env, InputStream inputStream, OutputStream outputStream, String param) {
        File file;
        String currentDirectory = env.get("PWD");
        if (param.length() == 0) {
            try {
                file = new File(currentDirectory);
                File[] folderEntries = file.listFiles();
                for (File entry : folderEntries) {
                    byte[] str = (entry.getAbsolutePath() + System.lineSeparator()).getBytes();
                    outputStream.write(str);
                }
            } catch (IOException e) {
                throw new CommandException("ls: cannot read/write from/to filestream");
            }
        }
        else {
            String path = param.trim();
            path = path.replace("\"", "");
            path = path.replace("'", "");
            // changes backslash to slash for universal path names
            path = path.replace("\\", "/");
            String targetPath = currentDirectory;
            targetPath = targetPath.replace("\\", "/");
            // create target path by part
            // use slash like label's home directory, because I use windows which doesn't react to ~
            if (path.startsWith("/")) {
                targetPath = System.getProperty("user.home");
                currentDirectory = targetPath;
                path = path.substring(1);
            }
            if (path.startsWith("C:")) {
                targetPath = path;
            }
            else {
                String[] pathParts = path.split("/");
                for (String pathPart : pathParts) {
                    if (pathPart.isEmpty()) continue;
                    if (pathPart.equals(".")) {
                        targetPath = currentDirectory;
                    }
                    if (pathPart.equals("..")) {
                        targetPath = new File(currentDirectory).getParent();
                    } else {
                        File directory = new File(currentDirectory);
                        List<File> files = Arrays.asList(directory.listFiles());
                        if (files.indexOf(new File(directory.getAbsolutePath() + "\\" + pathPart)) >= 0)
                            targetPath += "/" + pathPart;
                        else
                            targetPath = pathPart;
                    }
                    targetPath = targetPath.replace("\\", "/");
                    currentDirectory = targetPath;
                }
            }

            try {
                file = new File(targetPath);
                if (!file.exists()) {
                    outputStream.write(("ls: " + targetPath + ": No such file or directory"
                            + System.lineSeparator()).getBytes());
                    return;
                }
                if (!file.isDirectory()) {
                    outputStream.write(("ls: " + targetPath + ": Isn't a directory"
                            + System.lineSeparator()).getBytes());
                    return;
                }
                File[] folderEntries = file.listFiles();
                for (File entry : folderEntries) {
                    byte[] str = (entry.getAbsolutePath() + System.lineSeparator()).getBytes();
                    outputStream.write(str);
                }

            } catch (IOException e) {
                throw new CommandException("ls: cannot read/write from/to filestream");
            }
        }
    }
}
