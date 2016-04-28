package chapter_3_Template.learningtest;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
}
