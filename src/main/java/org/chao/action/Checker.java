package org.chao.action;

import java.time.LocalDate;

/**
 * 做一些上传次数限制,避免被人攻击
 * 
 * @author xiezhengchao
 * @since 2018/6/3 15:53
 */
public class Checker {

    private Box currentBox = new Box();

    public synchronized boolean check(String fileName) {
        int range = 10;
        int maxRange = 20;
        int chooseRange = fileName.contains("xiezhengchaoniubi") ? maxRange : range;

        String key = LocalDate.now().toString();

        if (!currentBox.currentLocalDate.equals(key)) {
            currentBox = new Box();
        }

        return currentBox.count++ < chooseRange;
    }

    private class Box {
        private String currentLocalDate;
        private int count;

        public Box() {
            this.currentLocalDate = LocalDate.now().toString();
        }
    }

}
