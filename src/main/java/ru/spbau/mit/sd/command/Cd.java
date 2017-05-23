package ru.spbau.mit.sd.command;

import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * class implements Runnable and changes current directory.
 */
public class Cd implements Runnable {
    /**
     * Implement method in Runnable.
     * if @param.length == 0 then does nothing
     * otherwise changes current directory to other which name take as parameter (param)
     * @param env - Environment
     * @param inputStream - stream to read data
     * @param outputStream - stream to write result
     * @param param parameters of command cat
     */
    @Override
    public void execute(Environment env, InputStream inputStream, OutputStream outputStream, String param) {
        if (param.length() != 0) {
            String path = param.trim();
            path = path.replace("\"", "");
            path = path.replace("'", "");
            // changes backslash to slash for universal path names
            path = path.replace("\\", "/");
            String currentDirectory = env.get("PWD");
            String targetPath = currentDirectory;
            targetPath = targetPath.replace("\\", "/");

            // create target path by part
            // use slash like label's home directory, because I use windows which doesn't react to ~
            if (path.startsWith("/")) {
                targetPath = System.getProperty("user.home");
                currentDirectory = targetPath;
                path = path.substring(1);
            }
            // windows doesn't show files in C:, that's why I use this hack
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
                        String a = directory.getAbsolutePath() + "\\" + pathPart;
                        if (files.indexOf(new File(directory.getAbsolutePath() + "\\" + pathPart)) >= 0)
                            targetPath += "/" + pathPart;
                        else
                            targetPath = pathPart;
                    }
                    targetPath = targetPath.replace("\\", "/");
                    currentDirectory = targetPath;
                }
            }


            File targetDirectory;
            try {
                targetDirectory = new File(targetPath);
                if (!targetDirectory.exists()) {
                    outputStream.write(("cd: " + targetPath + ": No such file or directory"
                            + System.lineSeparator()).getBytes());
                    return;
                }

                if (!targetDirectory.isDirectory()) {
                    outputStream.write(("cd: " + targetPath + ": Isn't a directory"
                            + System.lineSeparator()).getBytes());
                    return;
                }
                System.setProperty("user.dir", targetDirectory.getAbsolutePath());
                env.put("PWD", System.getProperty("user.dir"));
            } catch (IOException e) {
                throw new CommandException("cd: cannot read/write from/to filestream");
            }
        }
    }
}
