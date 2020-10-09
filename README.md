# comp
собрать проект

	mvn package 
	
собрать без тестов

	mvn package -DskipTests=true
	
запустить только тесты

	mvn test
	
запустить программу

	java -jar target/compiler-1.0.jar <опция> <файл>
	

	
опции
(результат лексера)

	--dump-tokens 

(результат парсера)

	--dump-ast 

(результат кодогенератора)

	--dump-asm
	
программы лежат в progs

лаб1:

	java -jar target/compiler-1.0.jar --dump-tokens  progs/Min.java
	
лаб2-лаб3:

	java -jar target/compiler-1.0.jar --dump-ast  progs/Min.java
	
лаб4:

	java -jar target/compiler-1.0.jar --dump-ast  progs/Min_eror.java
	
лаб5:

	java -jar target/compiler-1.0.jar --dump-asm  progs/Min.java
	
скомилировать программу

	java -jar target/compiler-1.0.jar 'название программы'
	
пример:

	java -jar target/compiler-1.0.jar  progs/Min.java


Запустить скомпилированную программу

	./bin/prog
