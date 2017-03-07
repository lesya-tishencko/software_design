package ru.spbau.mit.sd;


class Pwd extends Factory {

    /**
     * write current directory - where we run shell to ios
     * @param ios IOStream object for write and read
     * @param param parameters of command cat
     */
    @Override
    public void execute(IOStream ios, String param) {
        String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        ios.write(absolutePath.getBytes());
    }
}

