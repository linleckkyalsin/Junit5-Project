package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DemoApplicationTests {
    Calculator underTest=new Calculator();
	@Test
	void contextLoads() {
		int num1=11;
		int num2=1;
		int result = underTest.add(num1, num2);
		int expected=12;
		assertThat(result).isEqualTo(expected);



	}
	class Calculator{
		public int add(int a,int b){
			return a+b;
		}
	}


}
