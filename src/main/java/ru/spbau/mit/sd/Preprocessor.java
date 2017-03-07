package ru.spbau.mit.sd;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Preprocessor {
    private static Pattern patternForQuotes = Pattern.compile("([\"\']).*?\\1");
    private static Pattern patternForDoubleQuotes = Pattern.compile("\".*?\"");
    private static Pattern patternForGetVar = Pattern.compile("\\w+=\\w+");
    private static Pattern patternForSetVar = Pattern.compile("(\\$(\\w+))");
    private HashMap<String, String> variables = new HashMap<>();

    /**
     * preprocessing line to get variables
     * set variables value instead of $variables
     * @param line - input string
     * @return line after processing
     */
    String preprocess(String line) {

        line = removeDoubleSpace(line);
        line = getVariable(line);
        line = setVariable(line);
        line = removeDoubleSpace(line);

        return line;
    }

    private static String removeDoubleSpace(String line) {
        line = line.trim();
        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) == '"' || line.charAt(i) == '\'') {
                int j = i;
                ++i;
                while (i < line.length() && line.charAt(i) != line.charAt(j)) {
                    ++i;
                }
            }
            if (line.charAt(i) == ' ') {
                int j = i + 1;
                while (i < line.length() && line.charAt(i) == ' ') {
                    ++i;
                }
                line = line.substring(0, j) + line.substring(i);
                i = j - 1;
            }
        }
        return line;
    }


    private String getVariable(String line) {
        Matcher matchVar = patternForGetVar.matcher(line);
        int varStart = 0;
        Matcher matchQuotes = patternForQuotes.matcher(line);
        boolean hasQuotes = matchQuotes.find();

        while (matchVar.find(varStart)) {
            if (hasQuotes && matchVar.start() > matchQuotes.start()
                    && matchVar.end() < matchQuotes.end()) {
                varStart = matchVar.end();
                continue;
            } else {
                if (hasQuotes && matchVar.start() > matchQuotes.end()) {
                    hasQuotes = matchQuotes.find(matchQuotes.end());
                    continue;
                }
            }
            String temp = line.substring(matchVar.start(), matchVar.end()).trim();
            line = line.substring(0, matchVar.start()).concat(line.substring(matchVar.end()));
            varStart = matchVar.start();
            matchVar = matchVar.reset(line);
            int ind = temp.indexOf("=");
            variables.put(temp.substring(0, ind), temp.substring(ind + 1));
        }
        return line;
    }

    private String setVariable(String line) {
        Matcher matchQuotes = patternForDoubleQuotes.matcher(line);
        boolean hasQuotes = matchQuotes.find();
        Matcher matchVar = patternForSetVar.matcher(line);
        int varStart = 0;
        while (matchVar.find(varStart)) {
            if (!variables.containsKey(matchVar.group(2))) {
                line = line.substring(0, matchVar.start()).concat(line.substring(matchVar.end()));
                varStart = matchVar.start();
                matchVar = matchVar.reset(line);
                continue;
            }

            if (hasQuotes && matchVar.start() > matchQuotes.start()
                    && matchVar.end() < matchQuotes.end()) {
                line = line.substring(0, matchVar.start()).concat(variables.get(matchVar.group(2)))
                        .concat(line.substring(matchVar.end()));
                varStart = matchVar.start() + variables.get(matchVar.group(2)).length();
                matchVar = matchVar.reset(line);
                continue;
            }

            if (hasQuotes && matchVar.start() > matchQuotes.end()) {
                hasQuotes = matchQuotes.find(matchQuotes.end());
                continue;
            }

            line = line.substring(0, matchVar.start()).concat(variables.get(matchVar.group(2)))
                    .concat(line.substring(matchVar.end()));
            varStart = matchVar.start() + variables.get(matchVar.group(2)).length();
            matchVar = matchVar.reset(line);
        }
        return line;
    }

}

