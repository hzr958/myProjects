package com.smate.core.base.utils;

import java.util.Optional;

import org.junit.Test;

/**
 *
 * @author houchuanjie
 * @date 2018年3月16日 下午5:06:04
 */
public class Java8Test {

    @Test
    public void testOptional() {
        final User user = new User();
        user.setName("BOB");
        Optional<User> op = Optional.ofNullable(user);
        op.map(User::getName).map(String::toLowerCase).filter(s -> s.length() < 3).ifPresent(System.out::println);

        /* System.out.println("Full Name is set? " + op.isPresent());
        System.out.println("Full Name: " + op.orElseGet(() -> "[none]"));
        op.ifPresent(s -> System.out.println(s));
        System.out.println(op.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));*/

    }
}

class User {
    String name;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            要设置的 name
     */
    public void setName(String name) {
        this.name = name;
    }

}